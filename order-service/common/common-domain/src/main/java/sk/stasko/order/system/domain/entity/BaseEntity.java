package sk.stasko.order.system.domain.entity;

import java.util.Objects;

public class BaseEntity <ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return id.equals(that.id);
    }
}
