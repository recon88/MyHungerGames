package com.randude14.hungergames.reset;

import com.randude14.hungergames.Logging;
import com.randude14.hungergames.games.HungerGame;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.Rollback.RollbackType;
import uk.co.oliwali.HawkEye.SearchParser;
import uk.co.oliwali.HawkEye.callbacks.RollbackCallback;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchDir;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

/**
 *
 *
 */
public class HawkEyeResetter extends Resetter{
	Map<HungerGame, Long> startTimes = new HashMap<HungerGame, Long>();
	
	@Override
	public void init() {
	}

	@Override
	public void beginGame(HungerGame game) {
		startTimes.put(game, System.currentTimeMillis());
	}
    
	@Override
	public boolean resetChanges(HungerGame game) {
		SearchParser parser = new SearchParser();
		List<String> gamePlayers = new ArrayList<String>();
		for (Player p : game.getAllPlayers()) {
			gamePlayers.add(p.getName());
		}
		parser.players = gamePlayers;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		parser.dateFrom = sdf.format(new Date(startTimes.get(game)));
		HawkEyeAPI.performSearch(new RollbackCallback(new PlayerSession(new Logging.LogCommandSender("HawkEye")), RollbackType.LOCAL), parser, SearchDir.DESC);
		return true;
	}
}