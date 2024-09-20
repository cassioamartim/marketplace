package dev.martim.marketplace.market;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketType {

    DEFAULT("Market"),
    BLACK("Black Market");

    private final String name;
}
