package haze.utility.render

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:41.
object CustomRenderPipelines {
    /*private val HANDLE: MethodHandle =
        MethodHandles.publicLookup().findStaticGetter(
            VertexFormatElement::class.java,
            "MAX_COUNT",
            Int::class.java
        )

    private fun nextUnusedVFEIndex(): Int {
        try {
            val maxCount = HANDLE.invoke() as Int
            for (i in 0 until maxCount) {
                if (VertexFormatElement.byId(i) == null) {
                    return i
                }
            }
        } catch (ignored: Throwable) {
        }
        throw IllegalStateException("Not enough VertexFormatElement IDs available. This should not be possible unless other mods are broken.")
    }

    private val ROUNDING = VertexFormatElement.register(nextUnusedVFEIndex(), 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 4)
    private val SIZE = VertexFormatElement.register(nextUnusedVFEIndex(), 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 2)
    private val SMOOTHNESS = VertexFormatElement.register(nextUnusedVFEIndex(), 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1)
    private val OUTLINE_WIDTH = VertexFormatElement.register(nextUnusedVFEIndex(), 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1)

    val RECT = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
        .withLocation(HeshClient.of("rect"))
        .withVertexShader(HeshClient.of("core/rect"))
        .withFragmentShader(HeshClient.of("core/rect"))
        .withVertexFormat(VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("Rounding", ROUNDING)
            .add("Size", SIZE)
            .add("Smoothness", SMOOTHNESS)
            .build(),
            VertexFormat.Mode.QUADS)
        .build())

    fun addRounding(consumer: VertexConsumer, tl: Float, bl: Float, br: Float, tr: Float) {
        val offset = beginElement(consumer, ROUNDING)
        if (offset != -1L) {
            MemoryUtil.memPutFloat(offset, br)
            MemoryUtil.memPutFloat(offset + 4L, tr)
            MemoryUtil.memPutFloat(offset + 8L, bl)
            MemoryUtil.memPutFloat(offset + 12L, tl)
        }
    }

    fun addSize(consumer: VertexConsumer, width: Float, height: Float) {
        val offset = beginElement(consumer, SIZE)
        if (offset != -1L) {
            MemoryUtil.memPutFloat(offset, width)
            MemoryUtil.memPutFloat(offset + 4L, height)
        }
    }

    fun addOutlineWidth(consumer: VertexConsumer, outlineWidth: Float) {
        val offset = beginElement(consumer, OUTLINE_WIDTH)
        if (offset != -1L) {
            MemoryUtil.memPutFloat(offset, outlineWidth)
        }
    }

    fun addSmoothness(consumer: VertexConsumer, smoothness: Float) {
        val offset = beginElement(consumer, SMOOTHNESS)
        if (offset != -1L) {
            MemoryUtil.memPutFloat(offset, smoothness)
        }
    }

    private fun beginElement(consumer: VertexConsumer, element: VertexFormatElement): Long {
        if (consumer !is BufferBuilder)
            throw IllegalArgumentException("Not a BufferBuilder!")

        return (consumer as BufferBuilderAccessor).begin(element)
    }*/
}