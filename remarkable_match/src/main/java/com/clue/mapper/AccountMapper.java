package com.clue.mapper;

import com.clue.model.Account;
import com.clue.model.Assets;
import com.clue.service.Service;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

public class AccountMapper {
    public String newAccountQuery(String db, Account account) {
        double expRate = Service.meta.calculateExpRate(account.level + 1, account.exp);

        return String.format(
                "INSERT INTO `%s`.`account` (`uid`, `token`, `birth`, `level`, `exp`, `league`, `leaguePoint`, `country`,`lastPlayTime`) " +
                        "VALUES ('%s', '%s', '%d', '%d', '%f', '%d', '%d', '%s', '%d');",
                db,
                antiXss(account.uid),
                account.token,
                account.birth,
                account.level,
                expRate,
                account.league,
                account.leaguePoint,
                antiXss(account.country),
                account.lastPlayTime
        );
    }

    public String updateAccount(String db, Account account) {
        double expRate = Service.meta.calculateExpRate(account.level + 1, account.exp);
        return String.format(
                "UPDATE `%s`.`account` SET `name` = '%s', `level` = '%d', `exp` = '%f', `league` = '%d', `leaguePoint` = '%d', `country` = '%s' , `lastPlayTime` = '%d' " +
                        "WHERE `account`.`uid` = '%s'",
                db,
                antiXss(account.name),
                account.level,
                expRate,
                account.league,
                account.leaguePoint,
                antiXss(account.country),
                account.lastPlayTime,
                antiXss(account.uid)
        );
    }

    public String getAccountQuery(String db, String uid) {
        return String.format(
                "SELECT * FROM `%s`.`account` where `uid`='%s';",
                db,
                antiXss(uid)
        );
    }

    public String newAssetsQuery(String db, Assets assets) {
        return String.format(
                "INSERT INTO `%s`.`assets` (`uid`, `gold`, `ruby`) " +
                        "VALUES ('%s', '%d', '%d');",
                db,
                antiXss(assets.uid),
                assets.gold,
                assets.ruby
        );
    }

    public String getAssetsQuery(String db, String uid) {
        return String.format(
                "SELECT * FROM `%s`.`assets` where `uid`='%s';",
                db,
                antiXss(uid)
        );
    }

    public String updateAssetQuery(String db, String uid, Assets assets) {
        return String.format(
                "UPDATE `%s`.`assets` SET `gold` = %d, `ruby` = %d WHERE `uid` = '%s';",
                db,
                assets.gold,
                assets.ruby,
                antiXss(uid)
        );
    }

    public String increseAssetQuery(String db, String uid, int gold, int ruby) {
        return String.format(
                "UPDATE `%s`.`assets` SET `gold` = `gold` + '%d', `ruby` = `ruby` + '%d' WHERE `uid` = '%s';",
                db,
                gold,
                ruby,
                antiXss(uid)
        );
    }

    public Account parseAccount(ResultSet resultSet) {
        if (resultSet.getNumRows() != 1) {
            return null;
        }

        Account account = new Account();
        for (JsonArray row: resultSet.getResults()) {
            account.uid = row.getString(1);
            account.token = row.getInteger(2);
            account.name = row.getString(3);
            account.profileUrl = row.getString(4);
            account.birth = row.getLong(5);
            account.facebook = row.getString(6);
            account.google = row.getString(7);
            account.gameCenter = row.getString(8);
            account.level = row.getInteger(9);
            account.exp = Service.meta.calculateExp(account.level + 1, row.getDouble(10));
            account.league = (byte)(int)row.getInteger(11);
            account.leaguePoint = row.getInteger(12);
            account.country = row.getString(13);
            account.lastPlayTime = row.getLong(14);
            break;
        }

        return account;
    }

    public Assets parseAssets(ResultSet resultSet) {
        if (resultSet.getNumRows() != 1) {
            return null;
        }

        Assets assets = new Assets();
        for (JsonArray row: resultSet.getResults()) {
            assets.uid = row.getString(1);
            assets.gold = row.getInteger(2);
            assets.ruby = row.getInteger(3);
            break;
        }

        return assets;
    }

    public String antiXss(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("[\\\"';\\+]", "");
    }
}
