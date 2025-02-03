package sk.stasko.order.system.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public record StreetAddress(
        UUID id,
        String street,
        String postalCode,
        String city
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return street.equals(that.city) && street.equals(that.street) && postalCode.equals(that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
