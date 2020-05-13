package net.wytrem.spigot.exmachina;

import com.google.common.base.MoreObjects;

public class Script {
    private final String sourceCode;

    public Script(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sourceCode", sourceCode)
                .toString();
    }
}
