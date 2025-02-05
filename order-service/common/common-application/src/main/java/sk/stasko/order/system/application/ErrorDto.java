package sk.stasko.order.system.application;

import lombok.Builder;

@Builder
public record ErrorDto(String code, String message) {
}
