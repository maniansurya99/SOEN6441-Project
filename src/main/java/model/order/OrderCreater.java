package model.order;

import model.Country;
import model.GameMap;
import model.Player;
import utils.logger.LogEntryBuffer;

/**
 * A class to create Orders in the game.
 *
 * @author Neona Pinto
 * @author Dhananjay Narayan
 * @author Surya Manian
 * @author Madhuvanthi Hemanathan
 * @author Prathika Suvarna
 */
public class OrderCreater {
    public static GameMap d_GameMap = GameMap.getInstance();
    static LogEntryBuffer d_leb = new LogEntryBuffer();

    /**
     * A function to create an order
     *
     * @param p_commands the command entered
     * @param player     object parameter of type Player
     * @return the order
     */
    public static Order CreateOrder(String[] p_commands, Player player) {
        String l_Type = p_commands[0].toLowerCase();
        Order l_Order;
        switch (l_Type) {
            case "deploy":
                l_Order = new DeployOrder();
                l_Order.setOrderInfo(GenerateDeployOrderInfo(p_commands, player));
                break;
            case "advance":
                l_Order = new AdvanceOrder();
                l_Order.setOrderInfo(GenerateAdvanceOrderInfo(p_commands, player));
                break;
            case "negotiate":
                l_Order = new NegotiateOrder();
                l_Order.setOrderInfo(GenerateNegotiateOrderInfo(p_commands, player));
                break;
            case "blockade":
                l_Order = new BlockadeOrder();
                l_Order.setOrderInfo(GenerateBlockadeOrderInfo(p_commands, player));
                break;
            case "airlift":
                l_Order = new AirliftOrder();
                l_Order.setOrderInfo(GenerateAirliftOrderInfo(p_commands, player));
                break;
            case "bomb":
                l_Order = new BombOrder();
                l_Order.setOrderInfo(GenerateBombOrderInfo(p_commands, player));
                break;
            default:
                System.out.println("\nFailed to create an order due to invalid arguments");
                l_Order = null;

        }
        return l_Order;
    }

    /**
     * A function to generate the information of Deploying the order
     *
     * @param p_Command the command entered
     * @param p_Player  object parameter of type Player
     * @return the order information of deploy
     */
    private static OrderInfo GenerateDeployOrderInfo(String[] p_Command, Player p_Player) {
        String l_CountryID = p_Command[1];
        Country l_Country = d_GameMap.getCountry(l_CountryID);
        int l_NumberOfArmy = Integer.parseInt(p_Command[2]);
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_Player);
        l_OrderInfo.setDestination(l_Country);
        l_OrderInfo.setNumberOfArmy(l_NumberOfArmy);
        d_leb.logInfo("Player " + l_OrderInfo.getPlayer().getName() + " has given an order to deploy " + l_OrderInfo.getNumberOfArmy() + " armies to " + l_OrderInfo.getDestination().getName());
        return l_OrderInfo;
    }

    /**
     * A function to generate the information for advance order
     *
     * @param p_Command the command entered
     * @param p_Player  the player who issued the order
     * @return the order information of advance/attack
     */
    private static OrderInfo GenerateAdvanceOrderInfo(String[] p_Command, Player p_Player) {
        String l_FromCountryID = p_Command[1];
        Country l_FromCountry = d_GameMap.getCountry(l_FromCountryID);
        String l_ToCountryID = p_Command[2];
        Country l_ToCountry = d_GameMap.getCountry(l_ToCountryID);
        int l_NumberOfArmies = Integer.parseInt(p_Command[3]);
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_Player);
        l_OrderInfo.setDeparture(l_FromCountry);
        l_OrderInfo.setDestination(l_ToCountry);
        l_OrderInfo.setNumberOfArmy(l_NumberOfArmies);
        return l_OrderInfo;
    }


    /**
     * A function to generate the information of Negotiate order
     *
     * @param p_Command the command entered
     * @param p_Player  object parameter of type Player
     * @return the order information of deploy
     */
    private static OrderInfo GenerateNegotiateOrderInfo(String[] p_Command, Player p_Player) {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_Player);
        l_OrderInfo.setNeutralPlayer(d_GameMap.getPlayer(p_Command[1]));
        return l_OrderInfo;
    }

    /**
     * A function to generate information about Blockade Order
     *
     * @param p_command the command entered
     * @param p_player  object parameter of type Player
     * @return the order information of deploy
     */
    private static OrderInfo GenerateBlockadeOrderInfo(String[] p_command, Player p_player) {
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_player);
        String l_CountryID = p_command[1];
        Country l_TargetCountry = d_GameMap.getCountry(l_CountryID);
        l_OrderInfo.setTargetCountry(l_TargetCountry);

        return l_OrderInfo;
    }

    /**
     * function to generate information about Airlift Order
     *
     * @param p_command the command entered
     * @param p_player  object parameter of type Player
     * @return the order information of deploy
     */
    private static OrderInfo GenerateAirliftOrderInfo(String[] p_command, Player p_player) {
        String l_FromCountryID = p_command[1];
        Country l_FromCountry = d_GameMap.getCountry(l_FromCountryID);
        String l_ToCountryID = p_command[2];
        Country l_ToCountry = d_GameMap.getCountry(l_ToCountryID);
        int l_NumberOfArmies = Integer.parseInt(p_command[3]);
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_player);
        l_OrderInfo.setDeparture(l_FromCountry);
        l_OrderInfo.setDestination(l_ToCountry);
        l_OrderInfo.setNumberOfArmy(l_NumberOfArmies);
        return l_OrderInfo;
    }

    private static OrderInfo GenerateBombOrderInfo(String[] p_command, Player p_player){
        OrderInfo l_OrderInfo = new OrderInfo();
        l_OrderInfo.setPlayer(p_player);
        String l_CountryID = p_command[1];
        Country l_TargetCountry = d_GameMap.getCountry(l_CountryID);
        l_OrderInfo.setTargetCountry(l_TargetCountry);
        return l_OrderInfo;
    }

}
