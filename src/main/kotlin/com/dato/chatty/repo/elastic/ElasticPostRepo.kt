package com.dato.chatty.repo.elastic

import com.dato.chatty.model.elastic.ElasticPost
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ElasticPostRepo : ElasticsearchRepository<ElasticPost, String> {

    fun findByText(text: String, page: Pageable): List<ElasticPost>

}