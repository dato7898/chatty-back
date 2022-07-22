package com.dato.chatty.model.elastic

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "post")
data class ElasticPost(
    @Id val id: String?,
    val text: String
)