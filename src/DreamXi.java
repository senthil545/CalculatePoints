import static com.constants.AllConstants.BATSMAN_STATS;
import static com.constants.AllConstants.BOWLER_STATS;
import static com.constants.AllConstants.FIELDING_STATS;
import static com.constants.AllConstants.HTML_PATH;
import static com.constants.AllConstants.NON_TOPPERS_PER_ROW;
import static com.constants.AllConstants.TOTAL_PLAYERS;
import static com.constants.AllConstants.PLAYERS_COUNT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.lang.model.type.NullType;

import com.constants.AllConstants;

public class DreamXi {
	static Map<String, Integer> playerPoints;
	static Map<String, NullType> isPlaying = new HashMap<>();
	static double topperPoints;
	static String topper, runnerUp;
	//static String top20PlayerPoints = "";
	static Map<String,List<String>> pickedBy = new HashMap<>();

	static StringBuilder result;

	public static void createFile(String result) {
		String path = HTML_PATH;
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

		String html = "<html><head><meta http-equiv=\"refresh\" content=\"120\"><title>Dream11 : " + topper + ","
				+ runnerUp + " Leading </title></head><body><b><pre>" + result + "</pre></b>"
				+ "<br><br><br><font color= red><b>Last Updated at " + formatter.format(new Date())
				+ "</b></font><br><br><br><br>"
				//+ "<font size=10>TOP 20 PLAYERS</font>"+
				//top20PlayerPoints+
				+"</body></html>";
		try {
			FileWriter file = new FileWriter(path);
			file.write(html);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, Exception {
		executeDream11();
	}

	public static String executeDream11() throws Exception, InterruptedException {

		result = new StringBuilder();

		playerPoints = calculatePlayerPointsFromWeb();
		//top20PlayerPoints="";

		Map<String, DreamTeam> dreamTeams = fetchDreamXiTeams();
		Map<String, Double> finalPoints = new HashMap<String, Double>();
		for (String team : dreamTeams.keySet()) {
			DreamTeam teamsDomain = dreamTeams.get(team);

			finalPoints.put(team, teamsDomain.finalPoints);

		}

		finalPoints = DreamTeam.sortByValue(finalPoints);

		Map<String, DreamTeam> dreamTeamsSub = new LinkedHashMap<>(NON_TOPPERS_PER_ROW);
		Map<String, Double> finalPointsSub = new LinkedHashMap<>(NON_TOPPERS_PER_ROW);

		int count = 0;
		boolean isTopper = true;
		result.append("<h3>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 21, "-", false);
		result.append("<br>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("Dream11 Rankings", "", 21, "", false);
		result.append("<br>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 21, "-", false);
		result.append("<br>");
		int i = 1;
		result = result.append("<font color=\"DodgerBlue\">");
		double topperTempPoint = 0.0;
		for (String key : finalPoints.keySet()) {

			displayOnPage("", "", 60, "", false);
			result.append(String.format("%02d", i++));
			displayOnPage(key, String.format("%.2f", finalPoints.get(key)).toString(), 19, "|", false);
			if (i == 2) {
				topperTempPoint = finalPoints.get(key);
				result = result.append("</font>");
			} else {
				result = result.append("<font color=\"Red\">");
				result = result.append("&#8595 " + (topperTempPoint - finalPoints.get(key)));
				result = result.append("</font>");
			}
			result.append("<br>");

		}
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 21, "-", false);
		result.append("<br>");
		result.append("</h3>");
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");

		for (String key : finalPoints.keySet()) {
			dreamTeamsSub.put(key, dreamTeams.get(key));
			finalPointsSub.put(key, finalPoints.get(key));
			count++;
			if (count == NON_TOPPERS_PER_ROW) {
				displayNonToppers(dreamTeamsSub, finalPointsSub, isTopper);
				dreamTeamsSub.clear();
				finalPointsSub.clear();
				isTopper = false;
				count = 0;
			}
		}

		if (count > 0) {
			displayNonToppers(dreamTeamsSub, finalPointsSub, isTopper);
			count = 0;
		}
		
		HashMap<String , Double> tempMap = new HashMap<>();
		
		for(String key:dreamTeams.keySet()) {
		tempMap.put(key, dreamTeams.get(key).calculateTotalPoints());	
		}
		
		runnerUp = (String) DreamTeam.sortByValue(tempMap).keySet().toArray()[1];
		
		//String[] teamOwnerNames = dreamTeams.keySet().toArray(new String[dreamTeams.size()]);
		//runnerUp = teamOwnerNames[1];
		String winnerImage = "<img src=\"*****\"  height=\30%\" width=\"30%\" >";
		winnerImage = winnerImage.replace("*****", topper + ".jpg");
		String runnerImage = "<img src=\"*****\"  height=\30%\" width=\"30%\" >";
		runnerImage = runnerImage.replace("*****", runnerUp + ".jpg");
		createFile(winnerImage + runnerImage + result.toString());

		playerPoints.clear();
		isPlaying.clear();

		return result.toString();

	}

	private static void displayNonToppers(Map<String, DreamTeam> dreamTeams, Map<String, Double> finalPoints,
			boolean isTopper) {
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {

			displayOnPage(team, "", 20, "", false);

		}
		result.append("<br>");
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {

			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		for (int i = 0; i < TOTAL_PLAYERS; i++) {
			displayOnPage("", "", 42, "", false);
			for (String team : finalPoints.keySet()) {
				DreamTeam teamsDomain = dreamTeams.get(team);

				String player = teamsDomain.allPlayers.get(i);
				Double playerPoint = teamsDomain.allPlayersPoints.get(i);
				if (i >= 11) {
					result.append("<s><font color=red>");
				}
				displayOnPage(player, playerPoint.toString(), 20, "-", false);
				if (i >= 11) {
					result.append("</font></s>");
				}
			}
			result.append("<br>");

			if (i == 13) {

				displayOnPage("", "", 42, "", false);
				result.append("<br>");
			}

		}
		displayOnPage("", "", 42, "", false);
		for (String team : dreamTeams.keySet()) {
			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		result.append("<font size=3>");
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {
			DreamTeam teamsDomain = dreamTeams.get(team);
			if (!isTopper) {
				double diff = topperPoints - teamsDomain.finalPoints;
				displayOnPage(Double.toString(teamsDomain.finalPoints), "", 20, "(-" + Double.toString(diff) + ")",
						false);

			} else {
				topperPoints = teamsDomain.finalPoints;
				topper = teamsDomain.teamOwner;
				displayOnPage(Double.toString(teamsDomain.finalPoints), "", 20, "[TOP]", false);
				isTopper = false;
			}

		}
		result.append("</font>");
		result.append("<br>");

		displayOnPage("", "", 42, "", false);

		for (String team : dreamTeams.keySet()) {
			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");
	}

	private static void displayOnPage(String key, String value, int keyMaxSize, String delim, boolean onPrefix) {
		String keyValue = key + delim + value;
		String keyDisplay = "";
		if (keyValue.length() >= keyMaxSize) {
			keyValue = keyValue.substring(keyValue.length() - keyMaxSize);
		}
		keyDisplay = new String(new char[keyMaxSize - keyValue.length()]).replace('\0', ' ');
		if (onPrefix)
			result.append(keyValue + keyDisplay);
		else {
			if (isPlaying.containsKey(key) || isPlaying.containsKey(key.split("\\[")[0]))
				result.append("<font color= violet>" + keyDisplay + keyValue + "</font>");
			else
				result.append(keyDisplay + keyValue);
		}
		result.append("|");
	}

	private static Map<String, DreamTeam> fetchDreamXiTeams() throws FileNotFoundException, IOException {
		Map<String, DreamTeam> dreamTeamsMap = new LinkedHashMap<String, DreamTeam>();

		String dreamTeams = "IPLDream11";

		BufferedReader br = new BufferedReader(new FileReader(dreamTeams));

		String st;
		DreamTeam team = null;
		Map<String, Double> players12 = null;

		int count = 0;
		int type = 0;
		while ((st = br.readLine()) != null) {

			if (st.contains("Team")) {
				team = new DreamTeam();
				dreamTeamsMap.put(st.replace(" Team", ""), team);
				players12 = new HashMap<String, Double>(PLAYERS_COUNT);
				team.teamOwner = st.replace(" Team", "");
				team.players12 = (HashMap<String, Double>) players12;
				count = 0;
			} else {
				if (!st.isEmpty()) {
					String[] player = st.trim().split("\t");
					double x2x = 1;
					String playerKey = player[0];
					if (player.length > 0) {
						if (player[player.length - 1].equals("C")) {
							x2x = 2;
							playerKey = player[0] + "[C]";
						} else if (player[player.length - 1].equals("V")) {
							x2x = 1.5;
							playerKey = player[0] + "[V]";
						} else if (player[player.length - 1].equals("S")) {
							x2x = 1.25;
							playerKey = player[0] + "[S]";
						}
					}

					players12.put(playerKey, x2x * getOrDefault(playerPoints, player[0], team.teamOwner));
					count++;
					if (count > 13) {
						type = -1;
						team.calculateTotalPoints();
					}
				} else {
					type++;
				}
			}

		}
		
		int counter=20;
		for(String player: playerPoints.keySet()) {
			if(counter==0)
				break;
			List<String> temp = pickedBy.get(player);
			//top20PlayerPoints = top20PlayerPoints+"<br>"+player+ " : "+playerPoints.get(player)+"points , Picked by : "+temp;
			counter--;
			
		}
		
		
		return dreamTeamsMap;
	}

	static int getOrDefault(Map<String, Integer> playerPoints, String i , String teamOwner) {
		if (playerPoints.get(i) == null)
			return 0;
		List<String> pickedByOwners = pickedBy.get(i)==null?new ArrayList<String>():pickedBy.get(i);
		pickedByOwners.add(teamOwner);
		
			pickedBy.put(i , pickedByOwners);
			
		return playerPoints.get(i);
	}

	private static Map<String, Integer> calculatePlayerPointsFromWeb() throws IOException, InterruptedException {
		String batsmanFile = BATSMAN_STATS;
		String bowlerFile = BOWLER_STATS;
		String fieldingFile = FIELDING_STATS;
		// String runOutFile = "RunOuts";

		HashMap<String, Integer> playerStats = new HashMap<>();

		getStatsFromWeb(batsmanFile, playerStats, 6, 1);// runs
		getStatsFromWeb(batsmanFile, playerStats, 11, 16);// 100s
		getStatsFromWeb(batsmanFile, playerStats, 12, 8);// 50s
		getStatsFromWeb(bowlerFile, playerStats, 9, 25);// wickets
		getStatsFromWeb(bowlerFile, playerStats, 14, 8);// 4wcks
		getStatsFromWeb(bowlerFile, playerStats, 15, 16);// 5wcks
		getStatsFromWeb(bowlerFile, playerStats, 7, 8);// maidens
		getStatsFromWeb(fieldingFile, playerStats, 5, 8);// catches
		//getStatsFromWeb(fieldingFile, playerStats, 16, 12);// stumpings
		// getStats(runOutFile, playerStats, 1, 2, 6);// runouts
		
		playerStats = DreamTeam.sortByValue(playerStats);

		return playerStats;
	}

	private static Boolean getStatsFromWeb(String urlLink, Map<String, Integer> playerStats, int index,
			int pointsPerIndex) throws IOException {

		URL url = new URL(urlLink);

		URLConnection con = url.openConnection();
		con.addRequestProperty("User-Agent", 
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		InputStream is = con.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;
		String player = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.contains("AD Russell") || line.contains("KH Pandya")) {
				String allPlayersList[] = line.split(AllConstants.PLAYERS_DELIMITER);
				
				for(int i=1;i<allPlayersList.length;i++) {
					String playerInfo[] = allPlayersList[i].split("</span>");
					player = playerInfo[0].split("\\(")[0].trim();
					String playerPoint = playerInfo[index].replace("</td><td class=\"ds-min-w-max ds-text-right\"><span class=\"\">", "")
							.replace("<strong>", "").replace("</strong>", "");
					if (!playerStats.containsKey(player)) {
						playerStats.put(player, isInt(playerPoint) ? Integer.parseInt(playerPoint) * pointsPerIndex : 0);
					} else {
						playerStats.put(player, playerStats.get(player)
								+ (isInt(playerPoint) ? Integer.parseInt(playerPoint) * pointsPerIndex : 0));
					}
					if(playerInfo[3].trim().endsWith("*")) {
						isPlaying.put(player, null);
					}
					
				}
			}
			
		}
		return true;

	}

	private static boolean isInt(String score) {
		try {
			Integer.parseInt(score);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}	
