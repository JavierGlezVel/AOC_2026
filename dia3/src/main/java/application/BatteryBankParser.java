package application;

import domain.common.BatteryBank;

import java.util.ArrayList;
import java.util.List;

public class BatteryBankParser {
    public List<BatteryBank> parse(List<String> lines) {
        List<BatteryBank> banks = new ArrayList<>();

        for (String line : lines) {
            String ratings = line.trim();
            if (ratings.isEmpty()) {
                continue;
            }
            banks.add(new BatteryBank(ratings));
        }

        return banks;
    }
}
