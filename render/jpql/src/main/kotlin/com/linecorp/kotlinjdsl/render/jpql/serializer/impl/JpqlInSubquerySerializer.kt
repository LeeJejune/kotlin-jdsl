package com.linecorp.kotlinjdsl.render.jpql.serializer.impl

import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.impl.JpqlInSubquery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlRenderSerializer
import com.linecorp.kotlinjdsl.render.jpql.serializer.JpqlSerializer
import com.linecorp.kotlinjdsl.render.jpql.writer.JpqlWriter
import kotlin.reflect.KClass

class JpqlInSubquerySerializer : JpqlSerializer<JpqlInSubquery<*>> {
    override fun handledType(): KClass<JpqlInSubquery<*>> {
        return JpqlInSubquery::class
    }

    override fun serialize(part: JpqlInSubquery<*>, writer: JpqlWriter, context: RenderContext) {
        val delegate = context.getValue(JpqlRenderSerializer)

        delegate.serialize(part.value, writer, context)

        writer.write(" ")
        writer.write("IN")
        writer.write(" ")

        writer.writeParentheses {
            delegate.serialize(part.subquery, writer, context)
        }
    }
}
