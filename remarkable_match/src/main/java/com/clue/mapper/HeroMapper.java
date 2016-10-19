package com.clue.mapper;

import com.clue.model.Account;
import com.clue.model.Hero;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

import java.util.ArrayList;

public class HeroMapper {
    public String getHeroQuery(String db, String accountId, short name) {
        return String.format(
                "SELECT * FROM `%s`.`hero` where `accountId`='%s' and `name`=%d;",
                db,
                accountId,
                name
        );
    }

    public String getHeroesQuery(String db, String accountId) {
        return String.format(
                "SELECT * FROM `%s`.`hero` where `accountId`='%s';",
                db,
                accountId
        );
    }

    public String newHeroQuery(String db, String accountId, Hero hero) {
        return String.format(
                "INSERT INTO `%s`.`hero` (`accountId`, `name`, `skin`, `birth`) " +
                        "VALUES ('%s', '%d', '%d', '%d');",
                db,
                accountId,
                hero.name,
                hero.skin,
                hero.birth
        );
    }

    public Hero parseHero(ResultSet resultSet) {
        for (JsonArray row: resultSet.getResults()) {
            Hero hero = new Hero();
            hero.name = (short)(int)row.getInteger(2);
            hero.skin= row.getInteger(3);
            hero.birth = row.getLong(4);
            return hero;
        }

        return null;
    }

    public ArrayList<Hero> parseHeroes(ResultSet resultSet) {
        ArrayList<Hero> result = new ArrayList<>();
        for (JsonArray row: resultSet.getResults()) {
            Hero hero = new Hero();
            hero.name = (short)(int)row.getInteger(2);
            hero.skin= row.getInteger(3);
            hero.birth = row.getLong(4);
            result.add(hero);
        }

        return result;
    }
}
