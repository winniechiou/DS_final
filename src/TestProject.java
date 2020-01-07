import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestProject
 */
@WebServlet("/TestProject")
public class TestProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ArrayList<Keyword> keywords;
	public static boolean running_end = false;
	public static String[][] s;
	public static String google_search;
	public static ArrayList<WebNode> ListForSearchResults = new ArrayList<WebNode>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestProject() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if(request.getParameter("run") == null && request.getParameter("keyword")== null) {
			String requestUri = request.getRequestURI();
			System.out.println("222");
			request.setAttribute("requestUri", requestUri);
			request.getRequestDispatcher("Search.jsp").forward(request, response);
			return;
		}
		if(running_end) {
			running_end = false;
			System.out.println("here");
			request.setAttribute("query", s);
			request.setAttribute("relative keywords", google_search);
			request.getRequestDispatcher("googleitem.jsp")
			 .forward(request, response); 
			
		}else {
			
			response.getWriter().write("Searching...");
			response.setHeader("refresh", "3;url=http://localhost:8080/final/TestProject?run="+1);
			
			if(request.getParameter("keyword")!=null) {
				String keyword = request.getParameter("keyword").replaceAll(" ","+");
				System.out.println(keyword);
				running_end = false;
				System.out.println("in thread");
				keywords = new ArrayList();
				keywords.add(new Keyword("lyrics", 50));
				keywords.add(new Keyword("song", 5));
				keywords.add(new Keyword("composer", 3));
				keywords.add(new Keyword("album", 4));
				keywords.add(new Keyword("track", 4));
				keywords.add(new Keyword("artist", 4));
				keywords.add(new Keyword("vocal", 4));
				keywords.add(new Keyword("producer", 4));
				keywords.add(new Keyword("chorus", 50));
				keywords.add(new Keyword("verse", 4));
				keywords.add(new Keyword("written", 4));
				keywords.add(new Keyword("Apple Music", -1000));
				keywords.add(new Keyword("Apple_mobile", -1000));
				keywords.add(new Keyword("App Store", -100000));
				keywords.add(new Keyword("AppStore", -100000));
				keywords.add(new Keyword("wikipedia", -1000));
				keywords.add(new Keyword("facebook", -5000));
				keywords.add(new Keyword("維基", -1000));
				keywords.add(new Keyword("歌詞", 5));
				keywords.add(new Keyword("歌曲", 5));
				keywords.add(new Keyword("作曲", 3));
				keywords.add(new Keyword("作詞", 3));
				keywords.add(new Keyword("專輯", 4));
				keywords.add(new Keyword("歌手", 4));
				keywords.add(new Keyword("魔鏡", 2000));
				keywords.add(new Keyword("熱門歌曲", 4));
				keywords.add(new Keyword("所有專輯", 4));
				
				
				String teString = keyword;
				keywords.add(new Keyword(teString, 1000));
				String[] userInput = teString.split(" ");
				String input = "";
				for (String userinput:userInput) {
					input += userinput + "+";
				}
				GoogleQuery google = new GoogleQuery(input + "lyrics+genius+OR+魔鏡");
				HashMap<String, String> query;
				
				
				try {
					query = google.query();
					google_search = google.search();
					String relative = google_search;
					int index = relative.indexOf("：");
					relative = relative.substring(index+1);
					String[] relativeArray = relative.split("  ,");
					for (String relativeKeyword:relativeArray) {
						keywords.add(new Keyword(relativeKeyword, 4));
					}
				Subpage subpage = new Subpage();
				ListForSearchResults.clear();
				for (int m = 0; m<query.size(); m++) {
					//for (int m = 0; m<10; m++) {
						WebPage rootPage = new WebPage(query.get(query.keySet().toArray()[m]), (String) query.keySet().toArray()[m]);		
						WebTree tree = new WebTree(rootPage);
						ListForSearchResults.add(tree.root);
						System.out.println(ListForSearchResults.size());
						ArrayList<String> ListForSubpage = subpage.fetchSubpage(query.get(query.keySet().toArray()[m])); // list storing subpages for each result
						//System.out.println(query.getUrlList().get(10));
						//System.out.println(ListForSubpage.size());
						// int SubpageSize = subpage.getSubpageSize();
						if (ListForSubpage.size()==0) {
							System.out.println("No subpages");
						}
						else if (ListForSubpage.size()<2) {
							for (String sub : ListForSubpage) {
								tree.root.addChild(new WebNode(new WebPage(sub, "HI")));
							}
						}
						else {
							for (int s = 0; s<2; s++) {
								tree.root.addChild(new WebNode(new WebPage(ListForSubpage.get(s), "HI")));
							}
							System.out.println("Adding subpages is done.");
						}
						
						//for (int z = 0; z<2; z++) {
						//	System.out.println(tree.root.children.get(z).webPage.url);
						//}
						try {
							tree.setPostOrderScore(keywords);
						}catch (Exception e) {
							System.out.println("error: ");
							e.printStackTrace();
						}	
						System.out.println("Grading is done.");
						System.out.println("Score:" + tree.root.nodeScore);
						System.out.println();
					}
				//
					sort();
					Collections.reverse(ListForSearchResults);
					System.out.println("query.size"+query.size());
					System.out.println("ListForSearchResults.size"+ListForSearchResults.size());
					for (int x = 0; x<ListForSearchResults.size(); x++) {
						System.out.print(ListForSearchResults.get(x).nodeScore + "\t");
						System.out.println(ListForSearchResults.get(x).webPage.name);
					}
					s = new String[query.size()][2];
					
					int num = 0;
					//for(int a = 0; a<10; a++) {
					for(WebNode result:ListForSearchResults) {
					    s[num][0] = result.webPage.name;
					    s[num][1] = result.webPage.url;
					    num++;
					}
					running_end = true;
					keywords = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			
			}
			
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static void sort(){
		quickSort(0, ListForSearchResults.size()-1);
		System.out.println("Done");
	}
	
	
	public static void quickSort(int leftbound, int rightbound){
		//implement quickSort algorithm
		if (leftbound < rightbound) {
			double pivot = ListForSearchResults.get(rightbound).nodeScore; // rightbound -> pivot
			int count = leftbound-1; // how many keyword's count is smaller than that of pivot
			for (int m=leftbound; m<rightbound; m++) {
				if (ListForSearchResults.get(m).nodeScore <= pivot) {
					count++;
					swap(count, m);
				}
			}
			swap(count+1, rightbound); // put the pivot next to keyword whose count is less than its count
			quickSort(leftbound, count); // left
			quickSort(count+1, rightbound); // right
		}
	}
	
	
	public static void swap(int aIndex, int bIndex){
		WebNode temp = ListForSearchResults.get(aIndex);
		ListForSearchResults.set(aIndex, ListForSearchResults.get(bIndex));
		ListForSearchResults.set(bIndex, temp);
	}
//	class Work implements Runnable{
//		String keyword;
//		public Work(String keyword) {
//			this.keyword = keyword;
//		}
//		@Override
//		public void run() {
//			
//		
//	}
//	

}
