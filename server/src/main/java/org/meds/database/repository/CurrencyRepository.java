package org.meds.database.repository;

import org.meds.data.domain.Currency;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRepository extends MapRepository<Currency> implements Repository<Currency> {
}
