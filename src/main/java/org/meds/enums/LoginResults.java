package org.meds.enums;

import org.meds.util.Valued;

public enum LoginResults implements Valued {

    OutdatedVersion(0),
    OK(1),
    WrongLoginOrPassword(2),
    Banned(3),
    NotSoFast(4),
    InnerServerError(5),
    NeedReguild(6),
    TemporaryBan(7),
    PasswordExpired(8);

    private static LoginResults[] values = new LoginResults[9];

    static {
        for (LoginResults type : LoginResults.values())
            LoginResults.values[type.getValue()] = type;
    }

    public static LoginResults parse(int value) {
        return LoginResults.values[value];
    }

    private final int value;

    LoginResults(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
