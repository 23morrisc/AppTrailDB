import java.util.*;
import java.io.File;
import java.util.Collections;

public class TrailDatabase extends WaypointComparator{
    private ArrayList<Waypoint> database;
    private int searchTerm;
    private boolean asc;
    private ArrayList<Waypoint> temp;
    private WaypointComparator e;

    public TrailDatabase() {
        database = new ArrayList<>();
//        database.add(new Waypoint("FEATURE", "Springer Mt (3782 ft)", "GA", 0, 2174.6, 3782));
//        database.add(new Waypoint("FEATURE", "Clingmans Dome (6643 ft)", "TN", 195.3, 1973.3, 6643));
//        database.add(new Waypoint("FEATURE", "Mt Katahdin (5268 ft)", "ME", 2174.6, 0, 5268));
        populateDatabase();
    }

    public void populateDatabase() {
        try {
            Scanner in = new Scanner(new File("datafiles/apptrailDB.txt"));
            while (in.hasNext()) {
                String[] line = in.nextLine().split("\t");
                database.add(new Waypoint(line[0], line[1], line[2], Double.parseDouble(line[5]),
                        Double.parseDouble(line[6]), Integer.parseInt(line[7])));
            }

            in.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }

    public void printDB() {
        for(Waypoint w : database) {
            System.out.println(w);
        }
    }

    public void sortDB(){
        e = new WaypointComparator(searchTerm, asc);
        sort(database, e);
       // Collections.sort(database, new WaypointComparator(searchTerm, asc));
    }

    public void sort(ArrayList<Waypoint> a, WaypointComparator wc)   {
        int n = a.size();
        temp = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            temp.add(new Waypoint());
        }
        recursiveSort(a, 0, n-1, wc);
    }

    private void recursiveSort(ArrayList<Waypoint> a, int from, int to, WaypointComparator wc)   {

        if(to - from < 2)   {
            if(to > from && wc.compare(a.get(to),a.get(from)) < 0)   {
                Waypoint aTemp = a.get(to);
                a.set(to, a.get(from));
                a.set(from, aTemp);
            }
        }
        else {
            int middle = (from + to)/2;
            recursiveSort(a, from, middle, wc);
            recursiveSort(a, middle+1, to, wc);
            merge(a, from, middle, to, wc);
        }
    }

    private void merge(ArrayList<Waypoint> a, int from, int middle, int to, WaypointComparator wc)   {
        int i = from, j = middle + 1, k = from;
        while(i <= middle && j <= to)   {
            if(wc.compare(a.get(i), a.get(j)) < 0)    {
                temp.set(k, a.get(i));
                i++;
            }
            else {
                temp.set(k, a.get(j));
                j++;
            }
            k++;
        }
        while(i <= middle)  {
            temp.set(k, a.get(i));
            i++;
            k++;
        }
        while(j <= to)  {
            temp.set(k, a.get(j));
            j++;
            k++;
        }

        for(k = from; k <= to; k++)
            a.set(k, temp.get(k));
    }

    public void getSearchTerm() {
        System.out.println("+ Menu of search terms to use for sorting waypoints +\n" +
                "\tTY : by type\n" +
                "\tNA: by name\n" +
                "\tST : by state\n" +
                "\tDS: by distance to Springer\n" +
                "\tDK: by distance to Katahdin\n" +
                "\tEL: by elevation");
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your preferred sort by term or 'Q' to quit:");
        String term = in.nextLine();
        if (term.equals("TY")){
            searchTerm = 1;
        } else if (term.equals("NA")) {
            searchTerm = 2;
        } else if (term.equals("DS")) {
            searchTerm = 3;
        } else if (term.equals("ST")) {
        searchTerm = 4;
        } else if (term.equals("EL")) {
        searchTerm = 5;
        } else if (term.equals("NA")) {
        searchTerm = 6;
        } else {
            searchTerm = 0;
        }
        System.out.print("Enter 'A' to sort in ascending order or 'D' to sort in descending order: ");
        term = in.nextLine();
        asc = (term.equals("A")) ? true : false;
        //System.out.println("hey look at me " + searchTerm + " " + asc);
    }

    public static void main(String[] args) {
        TrailDatabase db = new TrailDatabase();
        System.out.println("*** Welcome to the Appalachian Trail Database ***");
        db.getSearchTerm();
        db.sortDB();
        db.printDB();
    }
}
