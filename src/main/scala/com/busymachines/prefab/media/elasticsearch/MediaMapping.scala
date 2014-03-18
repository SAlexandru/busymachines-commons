package com.busymachines.prefab.media.elasticsearch

import com.busymachines.commons.elasticsearch.ESMapping
import com.busymachines.prefab.media.domain.HashedMedia
import com.busymachines.prefab.media.domain.MediaDomainJsonFormats._
import com.busymachines.commons.domain.Id

object MediaMapping extends ESMapping[HashedMedia] {
  val id = "_id" -> "id" :: String.as[Id[HashedMedia]]
  val mimeType = "mimeType" :: String
  val name = "name" :: String
  val hash = "hash" :: String
  val data = "data" :: String & NotIndexed
}
