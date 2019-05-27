import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.List;

public class BaseballElimination {

    private int num; // 队伍数目
    private List<String> teams; //队名
    private int[] r; // 所剩比赛
    private int[] w; // 已经获胜的比赛数目
    private int[] l; // 已经失败的比赛数目
    private int[][] g; // i队和j队剩余比赛数目

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below

//        input example:
//        4
//        Atlanta       83 71  8  0 1 6 1
//        Philadelphia  80 79  3  1 0 0 2
//        New_York      78 78  6  6 0 0 0
//        Montreal      77 82  3  1 2 0 0

        //read input
        In in = new In(filename);
        num = in.readInt();
        String[] teams = new String[num];
        r = new int[num];
        w = new int[num];
        g = new int[num][num];
        for (int i = 0; i < num; i++) {
            teams[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < num; j++) {
                g[i][j]=in.readInt();
            }
        }
        this.teams = Arrays.asList(teams);
    }

    private FlowNetwork buildNetwork(int target){
        // construct graph
        FlowNetwork network = new FlowNetwork(1+num*(num-1)/2+num+1);
        int count=0;
        for (int i = 0; i < num; i++) {
            for (int j = i+1; j < num; j++) {
                FlowEdge layer1 = new FlowEdge(0,count+1,g[i][j]);
                network.addEdge(layer1);
                FlowEdge layer2_i = new FlowEdge(count+1,i+num*(num-1)/2+1,Double.MAX_VALUE);
                FlowEdge layer2_j = new FlowEdge(count+1,j+num*(num-1)/2+1,Double.MAX_VALUE);
                network.addEdge(layer2_i);
                network.addEdge(layer2_j);
                count++;
            }

            double ci = w[i]+r[i]-w[target];
            FlowEdge layer3 = new FlowEdge(
                    i+num*(num-1)/2+1,1+num*(num-1)/2+num,Double.max(ci,0));
            network.addEdge(layer3);
        }
//        System.out.println(network.toString());
        return network;
    }


    public int numberOfTeams(){
        // number of teams
        return num;
    }
    public Iterable<String> teams(){
        // all teams
        return teams;
    }
    public int wins(String team){
        // number of wins for given team
        return w[teams.indexOf(team)];
    }
    public int losses(String team){
        // number of losses for given team
        return l[teams.indexOf(team)];
    }
    public int remaining(String team){
        // number of remaining games for given team
        return r[teams.indexOf(team)];
    }
    public int against(String team1, String team2){
        // number of remaining games between team1 and team2
        return g[teams.indexOf(team1)][teams.indexOf(team2)];
    }
    public boolean isEliminated(String team){
        // is given team eliminated?
        int idx = teams.indexOf(team);

        return false;
    }
    public Iterable<String> certificateOfElimination(String team){
        // subset R of teams that eliminates given team; null if not eliminated
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }
}
