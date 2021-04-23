package test.com.pmrodrigues.rpg.factories;

import com.pmrodrigues.rpg.entities.BasicSecurityGroups;
import com.pmrodrigues.rpg.entities.Rule;
import com.pmrodrigues.rpg.repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Component
public class RuleFactory {

    @Autowired
    private RuleRepository repository;

    private Collection<Rule> rules;

    @PostConstruct
    public void postConstruct() {
        this.rules = createNewList();
        this.repository.saveAll(this.rules);
    }

    public Rule getRule(final String ruleName) {
        Optional<Rule> rule = rules.stream()
                .filter(r -> r.getAuthority().equalsIgnoreCase(ruleName) )
                .findFirst();

        if( !rule.isEmpty() ) return rule.get();

        return null;
    }

    public Collection<Rule> createNewList() {
        this.rules = new ArrayList<Rule>();

        rules.add( new Rule(BasicSecurityGroups.ADMIN) );
        rules.add( new Rule(BasicSecurityGroups.AUTHORS) );

        return rules;
    }

}
