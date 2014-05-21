package com.busymachines.prefab.media.domain

import com.busymachines.commons.domain.Id
import com.busymachines.commons.domain.HasId
import com.busymachines.commons.domain.MimeType

case class HashedMedia(
  id: Id[HashedMedia],
  mimeType: MimeType,
  name: Option[String],
  hash: String,
  data: String) extends HasId[HashedMedia]
