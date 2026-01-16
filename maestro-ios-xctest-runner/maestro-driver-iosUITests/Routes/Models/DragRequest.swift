import Foundation
import CoreGraphics

struct DragRequest: Decodable {

    enum CodingKeys: String, CodingKey {
        case startX, startY, endX, endY, duration, holdDuration, appIds
    }

    let start: CGPoint
    let end: CGPoint
    let duration: TimeInterval
    let holdDuration: TimeInterval
    let appIds: [String]?

    init(start: CGPoint, end: CGPoint, duration: TimeInterval, holdDuration: TimeInterval, appIds: [String]?) {
        self.start = start
        self.end = end
        self.duration = duration
        self.holdDuration = holdDuration
        self.appIds = appIds
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        start = CGPoint(
            x: try container.decode(Double.self, forKey: .startX),
            y: try container.decode(Double.self, forKey: .startY)
        )
        end = CGPoint(
            x: try container.decode(Double.self, forKey: .endX),
            y: try container.decode(Double.self, forKey: .endY)
        )
        duration = try container.decode(Double.self, forKey: .duration)
        holdDuration = try container.decodeIfPresent(Double.self, forKey: .holdDuration) ?? 0.3
        appIds = try container.decodeIfPresent([String].self, forKey: .appIds)
    }
}
