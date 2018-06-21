package temp;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }

public class LevelOrderSolution {
    private static List<List<Integer>> list = new ArrayList<List<Integer>>();

    public static void main(String[] args) {
        list.add(new ArrayList<Integer>());
    }
}