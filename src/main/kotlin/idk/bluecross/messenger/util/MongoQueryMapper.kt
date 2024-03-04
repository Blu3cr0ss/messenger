//package idk.bluecross.messenger.util
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import org.bson.Document
//import org.springframework.core.io.ClassPathResource
//import org.springframework.data.mongodb.core.aggregation.Aggregation
//import org.springframework.data.mongodb.core.aggregation.AggregationOperation
//import org.springframework.stereotype.Component
//
//@Component
//class MongoQueryMapper(val objectMapper: ObjectMapper) {
//    fun createAggregation(queryName: String, arguments: Map<String, String>): Aggregation {
//        var string = ClassPathResource("mongoQueries/$queryName").getContentAsString(Charsets.UTF_8) // get content
//        arguments.forEach {
//            string = string.replace("#" + it.key, it.value)
//        } // set variables
//        return Aggregation.newAggregation(listOf(AggregationOperation { ctx ->
//            ctx.getMappedObject(Document.parse(string))
//        }))
//    }
//}