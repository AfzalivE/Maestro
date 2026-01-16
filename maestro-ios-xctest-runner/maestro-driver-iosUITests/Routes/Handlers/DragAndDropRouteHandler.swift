import FlyingFox
import XCTest
import os

@MainActor
struct DragAndDropRouteHandler: HTTPHandler {
    private let logger = Logger(
        subsystem: Bundle.main.bundleIdentifier!,
        category: String(describing: Self.self)
    )

    func handleRequest(_ request: FlyingFox.HTTPRequest) async throws -> FlyingFox.HTTPResponse {
        guard let requestBody = try? await JSONDecoder().decode(DragRequest.self, from: request.bodyData) else {
            return AppError(type: .precondition, message: "incorrect request body provided for dragAndDrop request").httpResponse
        }

        if requestBody.duration < 0 || requestBody.holdDuration < 0 {
            return AppError(type: .precondition, message: "dragAndDrop duration/holdDuration cannot be negative").httpResponse
        }

        do {
            try await performDrag(requestBody)
            return HTTPResponse(statusCode: .ok)
        } catch {
            return AppError(message: "Drag and drop request failure. Error: \(error.localizedDescription)").httpResponse
        }
    }

    func performDrag(_ request: DragRequest) async throws {
        let (width, height) = ScreenSizeHelper.physicalScreenSize()
        let startPoint = ScreenSizeHelper.orientationAwarePoint(
            width: width,
            height: height,
            point: request.start
        )
        let endPoint = ScreenSizeHelper.orientationAwarePoint(
            width: width,
            height: height,
            point: request.end
        )

        let description = "Drag and drop from \(request.start) to \(request.end) with duration \(request.duration) and hold \(request.holdDuration)"
        logger.info("\(description)")

        let eventTarget = EventTarget()
        try await eventTarget.dispatchEvent(description: description) {
            EventRecord(orientation: .portrait)
                .addDragEvent(
                    start: startPoint,
                    end: endPoint,
                    holdDuration: request.holdDuration,
                    dragDuration: request.duration
                )
        }
    }
}
