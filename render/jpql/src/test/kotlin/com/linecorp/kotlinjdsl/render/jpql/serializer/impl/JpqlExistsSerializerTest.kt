package com.linecorp.kotlinjdsl.render.jpql.serializer.impl

import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entities
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressions
import com.linecorp.kotlinjdsl.querymodel.jpql.path.Paths
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicates
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.impl.JpqlExists
import com.linecorp.kotlinjdsl.querymodel.jpql.select.Selects
import com.linecorp.kotlinjdsl.render.TestRenderContext
import com.linecorp.kotlinjdsl.render.jpql.entity.book.Book
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlRenderSerializer
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlSerializerTest
import com.linecorp.kotlinjdsl.render.jpql.writer.JpqlWriter
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

@JpqlSerializerTest
class JpqlExistsSerializerTest : WithAssertions {
    private val sut = JpqlExistsSerializer()

    @MockK
    private lateinit var writer: JpqlWriter

    @MockK
    private lateinit var serializer: JpqlRenderSerializer

    private val subquery1 = Expressions.subquery(
        Selects.select(
            returnType = String::class,
            distinct = false,
            select = listOf(Paths.path(Book::title)),
            from = listOf(Entities.entity(Book::class)),
        ),
    )

    @Test
    fun handledType() {
        // when
        val actual = sut.handledType()

        // then
        assertThat(actual).isEqualTo(JpqlExists::class)
    }

    @Test
    fun serialize() {
        // given
        val part = Predicates.exists(subquery1)
        val context = TestRenderContext(serializer)

        // when
        sut.serialize(part as JpqlExists, writer, context)

        // then
        verifySequence {
            writer.write("EXISTS")
            writer.write(" ")
            writer.writeParentheses(any())
            serializer.serialize(subquery1, writer, context)
        }
    }
}
