import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DreamTeam {

	String teamOwner;
	HashMap<String, Double> players12;
	double finalPoints;
	List<String> allPlayers = new ArrayList<>(12);
	List<Double> allPlayersPoints = new ArrayList<>(12);

	Double calculateTotalPoints() {

		Map<String, Double> playerPoints = null;
		int count = 11;

		ArrayList<HashMap<String, Double>> list = new ArrayList<>();
		list.add(players12);

		HashMap<String, Double> playing11 = concatenateMap(list);

		sortAndCalculate(playerPoints, playing11, count);

		return finalPoints;
	}

	private void sortAndCalculate(Map<String, Double> playerPoints, HashMap<String, Double> players, int count) {
		playerPoints = sortByValue(players);
		calculatePoints(count, playerPoints);
		players = (HashMap<String, Double>) playerPoints;
	}

	private void calculatePoints(int count, Map<String, Double> playerPoints) {
		double finalPointsTemp = finalPoints;
		for (String player : playerPoints.keySet()) {
			String isPlayerInc = player;
			if (count <= 0) {
				count--;
				isPlayerInc = player;
			} else {
				finalPoints = finalPoints + playerPoints.get(player);
				count--;
			}
			allPlayers.add(isPlayerInc);
			allPlayersPoints.add(playerPoints.get(player));
		}
	}

	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return -1 * (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static HashMap<String, Double> concatenateMap(ArrayList<HashMap<String, Double>> mapList) {

		HashMap<String, Double> result = new HashMap<>();

		// iterate all maps
		for (HashMap<String, Double> map : mapList) {

			// iterate all keys from map
			for (String key : map.keySet()) {

				// if result already contains key, skip
				if (result.containsKey(key)) {
					continue;
				}

				result.put(key, map.get(key));
			}
		}

		return result;

	}
}