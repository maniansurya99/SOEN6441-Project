import model.GameMap;
import model.GamePhase;
import model.GameSettings;
import model.Player;
import model.strategy.game.GameStrategy;
import model.strategy.player.PlayerStrategy;
import model.tournament.TournamentOptions;
import model.tournament.TournamentResult;
import utils.MapReader;
import utils.MapValidation;
import utils.ValidationException;
import utils.logger.LogEntryBuffer;

import java.util.*;

public class TournamentEngine implements Engine {

    private final LogEntryBuffer d_Logger;
    TournamentOptions d_Options;
    List<TournamentResult> d_Results = new ArrayList<>();
    GameMap d_CurrentMap;

    public TournamentEngine() {
        d_Logger = LogEntryBuffer.getInstance();
        d_Options = getTournamentOptions();
    }

    private TournamentOptions getTournamentOptions() {
        Scanner l_Scanner = new Scanner(System.in);
        d_Logger.log("You are in Tournament Mode");
        d_Logger.log("enter the tournament command");
        String l_TournamentCommand = l_Scanner.nextLine();
        return parseCommand(l_TournamentCommand);
    }

    private TournamentOptions parseCommand(String p_TournamentCommand) {
        try {
            d_Options = new TournamentOptions();
            if (!p_TournamentCommand.isEmpty() &&
                    p_TournamentCommand.contains("-M") && p_TournamentCommand.contains("-P")
                    && p_TournamentCommand.contains("-G") && p_TournamentCommand.contains("-D")) {
                List<String> l_CommandList = Arrays.asList(p_TournamentCommand.split(" "));
                String l_MapValue = l_CommandList.get(l_CommandList.indexOf("-M") + 1);
                String l_PlayerTypes = l_CommandList.get(l_CommandList.indexOf("-P") + 1);
                String l_GameCount = l_CommandList.get(l_CommandList.indexOf("-G") + 1);
                String l_maxTries = l_CommandList.get(l_CommandList.indexOf("-D") + 1);
                d_Options.getMap().addAll(Arrays.asList(l_MapValue.split(",")));
                for (String l_Strategy : l_PlayerTypes.split(",")) {
                    d_Options.getPlayerStrategies().add(PlayerStrategy.getStrategy(l_Strategy));
                }
                d_Options.setGames(Integer.parseInt(l_GameCount));
                d_Options.setMaxTries(Integer.parseInt(l_maxTries));
            }
            return d_Options;
        } catch (Exception e) {
            d_Logger.log("Check your command");
            d_Logger.log("command should be in this format: tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D maxnumberofturns");
            return getTournamentOptions();
        }
    }

    public void start() throws ValidationException {
        for (String l_File : d_Options.getMap()) {
            for (int l_game = 1; l_game <= d_Options.getGames(); l_game++) {
                GameSettings.getInstance().MAX_TRIES = d_Options.getMaxTries();
                d_CurrentMap = GameMap.getInstance();
                d_CurrentMap.flushGameMap();
                TournamentResult l_Result = new TournamentResult();
                d_Results.add(l_Result);
                l_Result.setGame(l_game);
                l_Result.setMap(l_File);
                MapReader.readMap(d_CurrentMap, l_File);
                if (!MapValidation.validateMap(d_CurrentMap, 0)) {
                    throw new ValidationException("Invalid Map");
                }
                for (PlayerStrategy l_PlayerStrategy : d_Options.getPlayerStrategies()) {
                    Player l_Player = new Player(l_PlayerStrategy);
                    l_Player.setName(l_PlayerStrategy.getClass().getSimpleName());
                    d_CurrentMap.getPlayers().put(l_PlayerStrategy.getClass().getSimpleName(), l_Player);
                }
                d_CurrentMap.assignCountries();
                GameEngine l_GameEngine = new GameEngine();
                l_GameEngine.setGamePhase(GamePhase.Reinforcement);
                l_GameEngine.start();
                Player l_Winner = d_CurrentMap.getWinner();
                if (Objects.nonNull(l_Winner)) {
                    l_Result.setWinner(l_Winner.getName());
                } else {
                    l_Result.setWinner("Draw");
                }
                d_CurrentMap.flushGameMap();
            }
        }

        for (TournamentResult l_Result : d_Results) {
            System.out.printf("%15s%15s", l_Result.getMap(), l_Result.getWinner());
        }

    }

    //tournament -M Australia.map,newmap.map -P aggressive,random -G 2 -D 3
    @Override
    public void setGamePhase(GamePhase p_GamePhase) {

    }
}
