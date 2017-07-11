package org.meds.spell;

import org.meds.Unit;
import org.meds.database.Repository;
import org.meds.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpellFactory {

    @Autowired
    private Repository<org.meds.data.domain.Spell> spellRepository;

    public Spell create(int spellId, Unit caster, int effectLevel, Unit target, Item item) {
        org.meds.data.domain.Spell spellEntry = spellRepository.get(spellId);
        return new Spell(spellEntry, caster, effectLevel, target, item);
    }

    public Spell create(int spellId, Unit caster, int effectLevel, Unit target) {
        org.meds.data.domain.Spell spellEntry = spellRepository.get(spellId);
        return new Spell(spellEntry, caster, effectLevel, target, null);
    }

    public Spell createSelf(int spellId, Unit caster, int effectLevel, Item item) {
        return create(spellId, caster, effectLevel, caster, item);
    }

    public Spell createSelf(int spellId, Unit caster, int effectLevel) {
        return create(spellId, caster, effectLevel, null, null);
    }
}
