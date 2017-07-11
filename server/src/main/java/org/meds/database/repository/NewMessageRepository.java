package org.meds.database.repository;

import org.meds.data.domain.NewMessage;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class NewMessageRepository extends MapRepository<NewMessage> implements Repository<NewMessage> {
}
