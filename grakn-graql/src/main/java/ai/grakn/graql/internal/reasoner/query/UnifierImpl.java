/*
 * Grakn - A Distributed Semantic Database
 * Copyright (C) 2016  Grakn Labs Limited
 *
 * Grakn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Grakn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Grakn. If not, see <http://www.gnu.org/licenses/gpl.txt>.
 */

package ai.grakn.graql.internal.reasoner.query;

import ai.grakn.graql.Var;
import ai.grakn.graql.admin.Unifier;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * Implementation of {@link Unifier} interface.
 * </p>
 *
 * @author Kasper Piskorski
 *
 */
public class UnifierImpl implements Unifier {

    //TODO turn it to multimap to accommodate all cases
    private final Map<Var, Var> unifier = new HashMap<>();

    public UnifierImpl(){}
    public UnifierImpl(Map<Var, Var> map){
        unifier.putAll(map);
    }
    public UnifierImpl(Unifier u){
        unifier.putAll(u.map());
    }

    @Override
    public String toString(){
        return unifier.toString();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || this.getClass() != obj.getClass()) return false;
        if (obj == this) return true;
        UnifierImpl u2 = (UnifierImpl) obj;
        return unifier.equals(u2.map());
    }

    @Override
    public int hashCode(){
        return unifier.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return unifier.isEmpty();
    }

    @Override
    public Map<Var, Var> map() {
        return Maps.newHashMap(unifier);
    }

    @Override
    public Set<Var> keySet() {
        return unifier.keySet();
    }

    @Override
    public Collection<Var> values() {
        return unifier.values();
    }

    @Override
    public Set<Map.Entry<Var, Var>> getMappings(){ return unifier.entrySet();}

    public Var addMapping(Var key, Var value){
        return unifier.put(key, value);
    }

    @Override
    public Var get(Var key) {
        return unifier.get(key);
    }

    @Override
    public boolean containsKey(Var key) {
        return unifier.containsKey(key);
    }

    @Override
    public boolean containsValue(Var value) {
        return unifier.containsValue(value);
    }

    @Override
    public boolean containsAll(Unifier u) { return getMappings().containsAll(u.getMappings());}

    @Override
    public Unifier merge(Unifier d) {
        unifier.putAll(d.map());
        return this;
    }

    @Override
    public Unifier removeTrivialMappings() {
        Set<Var> toRemove = unifier.entrySet().stream()
                .filter(e -> e.getKey() == e.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        toRemove.forEach(unifier::remove);
        return this;
    }

    @Override
    public Unifier invert() {
        return new UnifierImpl(
                unifier.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))
        );
    }

    @Override
    public int size(){ return unifier.size();}
}
