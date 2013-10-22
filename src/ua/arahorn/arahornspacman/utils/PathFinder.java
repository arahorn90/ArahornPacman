package ua.arahorn.arahornspacman.utils;

import java.util.ArrayList;
import java.util.Arrays;

import ua.arahorn.arahornspacman.GameActivity;

public class PathFinder {

	public class Point {

		private int x;
		private int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Point))
				return false;
			return (((Point) o).getX() == x) && (((Point) o).getY() == y);
		}

		@Override
		public int hashCode() {
			return Integer.valueOf(x) ^ Integer.valueOf(y);
		}

		@Override
		public String toString() {
			return "x: " + Integer.valueOf(x).toString() + " y:" + Integer.valueOf(y).toString();
		}
	};

	int[][] fillmap;

	ArrayList<Point> path = new ArrayList<Point>();

	int[][] labyrinth; 
	ArrayList<Point> buf = new ArrayList<Point>();

	public PathFinder(int[][] labyrinth) {

		this.labyrinth = labyrinth.clone();
		fillmap = new int[GameActivity.mapHeight][GameActivity.mapWidth];

	}
	
	public void setOpen(boolean isOpen){
		if(isOpen) {
			labyrinth[7][9] = 1;
		} else {
			labyrinth[7][9] = 0;
		}
	}

	void push(Point p, int n) {
		if (fillmap[p.getY()][p.getX()] <= n)
			return;
		fillmap[p.getY()][p.getX()] = n;
		buf.add(p);
	}

	Point pop() {
		if (buf.isEmpty())
			return null;
		return (Point) buf.remove(0);
	}

	public Point[] find(Point start, Point end) {

		int tx = 0, ty = 0, n = 0, t = 0;
		Point p;

		for (int i = 0; i < fillmap.length; i++)
			Arrays.fill(fillmap[i], Integer.MAX_VALUE);
		push(start, 0);

		while ((p = pop()) != null) {
			if (p.equals(end)) {
			}
			n = fillmap[p.getY()][p.getX()] + 1;

			if ((p.getY() + 1 < labyrinth.length) && labyrinth[p.getY() + 1][p.getX()] != 0)
				push(new Point(p.getX(), p.getY() + 1), n);
			if ((p.getY() - 1 >= 0) && (labyrinth[p.getY() - 1][p.getX()] != 0))
				push(new Point(p.getX(), p.getY() - 1), n);
			if ((p.getX() + 1 < labyrinth[p.getY()].length) && (labyrinth[p.getY()][p.getX() + 1] != 0))
				push(new Point(p.getX() + 1, p.getY()), n);
			if ((p.getX() - 1 >= 0) && (labyrinth[p.getY()][p.getX() - 1] != 0))
				push(new Point(p.getX() - 1, p.getY()), n);
		}

		if (fillmap[end.getY()][end.getX()] == Integer.MAX_VALUE) {
			System.err.println("not found path");
			return null;
		}
		path.clear();
		path.add(end);
		int x = end.getX();
		int y = end.getY();
		n = Integer.MAX_VALUE;

		while ((x != start.getX()) || (y != start.getY())) {
			if (fillmap[y + 1][x] < n) {
				tx = x;
				ty = y + 1;
				t = fillmap[y + 1][x];
			}
			if (fillmap[y - 1][x] < n) {
				tx = x;
				ty = y - 1;
				t = fillmap[y - 1][x];
			}
			if (fillmap[y][x + 1] < n) {
				tx = x + 1;
				ty = y;
				t = fillmap[y][x + 1];
			}
			if (fillmap[y][x - 1] < n) {
				tx = x - 1;
				ty = y;
				t = fillmap[y][x - 1];
			}

			x = tx;
			y = ty;
			n = t;
			path.add(new Point(x, y));
		}

		Point[] result = new Point[path.size()];
		t = path.size();
		for (Object point : path) {
			result[--t] = (Point) point;
		}
		return result;
	}
}
