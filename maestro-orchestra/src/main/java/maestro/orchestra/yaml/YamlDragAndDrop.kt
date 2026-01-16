package maestro.orchestra.yaml

import com.fasterxml.jackson.annotation.JsonFormat
import maestro.orchestra.ElementSelector
import maestro.orchestra.util.Env.evaluateScripts
import maestro.orchestra.DragAndDropCommand
import maestro.js.JsEngine

@JsonFormat(with = [JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES])
data class YamlDragAndDrop(
    val from: YamlElementSelectorUnion,
    val to: YamlElementSelectorUnion,
    val duration: Long? = null,
    val holdDurationMs: Long? = null,
    val label: String? = null,
    val optional: Boolean = false,
    val waitToSettleTimeoutMs: Int? = null,
) {
    fun toCommand(fromSelector: ElementSelector, toSelector: ElementSelector): DragAndDropCommand {
        return DragAndDropCommand(
            from = fromSelector,
            to = toSelector,
            duration = duration ?: DragAndDropCommand.DEFAULT_DURATION_IN_MILLIS,
            holdDurationMs = holdDurationMs ?: DragAndDropCommand.DEFAULT_HOLD_DURATION_IN_MILLIS,
            label = label,
            optional = optional,
            waitToSettleTimeoutMs = waitToSettleTimeoutMs,
        )
    }

    fun evaluateScripts(jsEngine: JsEngine): YamlDragAndDrop {
        return copy(
            from = (from as? YamlElementSelector)?.evaluateScripts(jsEngine) ?: from,
            to = (to as? YamlElementSelector)?.evaluateScripts(jsEngine) ?: to,
            label = label?.evaluateScripts(jsEngine)
        )
    }
}
