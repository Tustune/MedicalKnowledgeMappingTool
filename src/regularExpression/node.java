package regularExpression;

public class node {
	int rank;
	String name;
	String content;

	public node() {
	}

	public node(int rank, String name, String content) {
		// TODO Auto-generated constructor stub
		this.rank = rank;
		this.name = name;
		this.content = content;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
