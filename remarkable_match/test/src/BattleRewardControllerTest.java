import com.clue.controller.BattleRewardController;
import com.clue.fbs.RmsLeagueBalance;
import com.clue.fbs.RmsVariable;
import com.clue.model.Account;
import com.clue.model.BattleRewardResultSet;
import com.clue.model.Player;
import com.clue.model.Room;
import com.clue.service.MetaServiceImpl;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BattleRewardControllerTest {
    BattleRewardController controller = new BattleRewardController();

    Room makeTestRoom(int leagePoint1, int leagePoint2) {
        Room room = new Room("test room", (byte)0);
        Player player0 = new Player();
        player0.setLeaguePoint((short)leagePoint1);
        player0.setUid("player0");
        player0.setPlayerNo((byte)0);

        Player player1 = new Player();
        player1.setLeaguePoint((short)leagePoint2);
        player1.setUid("player0");
        player1.setPlayerNo((byte)1);

        Account account0 = new Account();
        account0.uid = player0.getUid();
        account0.leaguePoint = player0.getLeaguePoint();
        player0.setAccount(account0);

        Account account1 = new Account();
        account1.uid = player1.getUid();
        account1.leaguePoint = player1.getLeaguePoint();
        player1.setAccount(account1);

        room.setPlayer0(player0);
        room.setPlayer1(player1);
        return room;
    }

    @org.junit.Before
    public void setUp() throws Exception {
        MetaServiceImpl metaService = mock(MetaServiceImpl.class);
        RmsLeagueBalance leagueBalance0 = mock(RmsLeagueBalance.class);
        RmsLeagueBalance leagueBalance1 = mock(RmsLeagueBalance.class);
        RmsLeagueBalance leagueBalance2 = mock(RmsLeagueBalance.class);
        RmsVariable variable = mock(RmsVariable.class);
        //RmsLeagueBalance getLeagueBalance(int league);

        controller.setMetaService(metaService);
        when(variable.winExp()).thenReturn((short)10);
        when(variable.drawExp()).thenReturn((short)4);
        when(variable.loseExp()).thenReturn((short)3);
        when(variable.maxLeague()).thenReturn((short)1);
        when(variable.maxLevel()).thenReturn((short)2);
        when(leagueBalance0.gold()).thenReturn((short)30);
        when(leagueBalance1.point()).thenReturn(30);
        when(leagueBalance2.point()).thenReturn(50);
        when(metaService.getLeagueBalance(0)).thenReturn(leagueBalance0);
        when(metaService.getLeagueBalance(1)).thenReturn(leagueBalance1);
        when(metaService.getLeagueBalance(2)).thenReturn(leagueBalance2);
        when(metaService.getExp(1)).thenReturn(0);
        when(metaService.getExp(2)).thenReturn(10);
        when(metaService.getVariable()).thenReturn(variable);
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void calculateRewardPlayer0Win() throws Exception {
        Room room = makeTestRoom(0, 0);
        BattleRewardResultSet result = controller.calculateReward(room, (byte) 0);
        Assert.assertEquals(result.player0.league, 1);
        Assert.assertEquals(result.player0.leaguePoint, 30);
        Assert.assertEquals(result.player0.exp, 10);
        Assert.assertEquals(result.player0.gold, 30);
        Assert.assertEquals(result.player1.league, 0);
        Assert.assertEquals(result.player1.leaguePoint, 0);
        Assert.assertEquals(result.player1.exp, 3);
        Assert.assertEquals(result.player1.gold, 0);
        Assert.assertEquals(result.gold, 30);

        Assert.assertEquals(result.account0.level, 2);
        Assert.assertEquals(result.account0.exp, 0);
        Assert.assertEquals(result.account1.level, 1);
        Assert.assertEquals(result.account1.exp, 3);
        Assert.assertEquals(result.account0.league, 1);
        Assert.assertEquals(result.account0.leaguePoint, 30);
        Assert.assertEquals(result.account1.league, 0);
        Assert.assertEquals(result.account1.leaguePoint, 0);
    }

    @org.junit.Test
    public void calculateRewardPlayer1Win() throws Exception {
        Room room = makeTestRoom(0, 0);
        BattleRewardResultSet result = controller.calculateReward(room, (byte) 1);
        Assert.assertEquals(result.player0.league, 0);
        Assert.assertEquals(result.player0.leaguePoint, 0);
        Assert.assertEquals(result.player0.exp, 3);
        Assert.assertEquals(result.player0.gold, 0);
        Assert.assertEquals(result.player1.league, 1);
        Assert.assertEquals(result.player1.leaguePoint, 30);
        Assert.assertEquals(result.player1.exp, 10);
        Assert.assertEquals(result.player1.gold, 30);
        Assert.assertEquals(result.gold, 30);

        Assert.assertEquals(result.account0.level, 1);
        Assert.assertEquals(result.account0.exp, 3);
        Assert.assertEquals(result.account1.level, 2);
        Assert.assertEquals(result.account1.exp, 0);
        Assert.assertEquals(result.account0.league, 0);
        Assert.assertEquals(result.account0.leaguePoint, 0);
        Assert.assertEquals(result.account1.league, 1);
        Assert.assertEquals(result.account1.leaguePoint, 30);
    }

    @org.junit.Test
    public void calculateRewardPlayer1Draw() throws Exception {
        Room room = makeTestRoom(0, 0);
        BattleRewardResultSet result = controller.calculateReward(room, (byte)-1);
        Assert.assertEquals(result.player0.leaguePoint, 0);
        Assert.assertEquals(result.player0.exp, 4);
        Assert.assertEquals(result.player0.gold, 0);
        Assert.assertEquals(result.player1.leaguePoint, 0);
        Assert.assertEquals(result.player1.exp, 4);
        Assert.assertEquals(result.player1.gold, 0);
        Assert.assertEquals(result.gold, 30);

        Assert.assertEquals(result.account0.level, 1);
        Assert.assertEquals(result.account0.exp, 4);
        Assert.assertEquals(result.account1.level, 1);
        Assert.assertEquals(result.account1.exp, 4);
        Assert.assertEquals(result.account0.league, 0);
        Assert.assertEquals(result.account0.leaguePoint, 0);
        Assert.assertEquals(result.account1.league, 0);
        Assert.assertEquals(result.account1.leaguePoint, 0);
    }
}