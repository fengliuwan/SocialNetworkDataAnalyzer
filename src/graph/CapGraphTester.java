package graph;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import util.GraphLoader;


public class CapGraphTester {
	CapGraph g1;
	
	@Before
	public void setUp() throws Exception {
		g1 = new CapGraph();
		GraphLoader.loadGraph(g1, "data/small_test_graph.txt");
	}

	@Test
	public void testAdd() throws Exception {
		System.out.println(g1);
	}
}
