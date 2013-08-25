package com.busymachines.prefab.authentication.http

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.busymachines.commons.CommonConfig
import com.busymachines.commons.domain.Id
import com.busymachines.prefab.authentication.logic.PrefabAuthenticator
import com.busymachines.prefab.authentication.model.Authentication
import spray.http.HttpCredentials
import spray.http.HttpHeader
import spray.http.HttpRequest
import spray.routing.AuthenticationFailedRejection
import spray.routing.AuthenticationFailedRejection._
import spray.routing.HttpService._
import spray.routing.RequestContext
import spray.routing.authentication.HttpAuthenticator
import spray.routing.authentication.ContextAuthenticator
import spray.routing.directives.AuthMagnet

trait AuthenticationDirectives {
  
  val tokenKey = "Auth-Token".toLowerCase

  implicit def toAuthentication[SecurityContext](authenticator : PrefabAuthenticator[_, SecurityContext])(implicit ec : ExecutionContext) = {
    AuthMagnet.fromContextAuthenticator {
      new HttpAuthenticator[SecurityContext] {
        override def apply(ctx : RequestContext) = {
          def devmodeAuth = {
              if (CommonConfig.devmode) authenticator.devmodeSecurityContext(ctx.request.uri.query.get("devmode-auth"))
              else None
          }
        
          ctx.request.headers.find(_.is(tokenKey)).map(_.value).map(Id[Authentication](_)) match {
            case Some(authenticationId) =>
              authenticator.authenticate(authenticationId) map {
                case Some(securityContext) => Right(securityContext)
                case None =>
                  devmodeAuth match {
                    case Some(devContext) => Right(devContext)
                    case none => Left(AuthenticationFailedRejection(CredentialsRejected, this))
                  }
              }
            case None =>
              devmodeAuth match {
                case Some(devContext) => Future.successful(Right(devContext))
                case none => Future.successful(Left(AuthenticationFailedRejection(CredentialsMissing, this)))
              }
          }      
        }
        def executionContext: ExecutionContext = ec
    
        def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext): Future[Option[SecurityContext]] = ???
      
        def getChallengeHeaders(httpRequest: HttpRequest): List[HttpHeader] = Nil
      }
    }
  }
  
}