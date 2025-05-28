package example.saletaxes.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaxesRate {
    Imported(5.0),
    Exempt(0.0),
    Goods(10.0);

    private final double rate;
}
