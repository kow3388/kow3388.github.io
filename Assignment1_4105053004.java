package test1;

import java.util.Random;
import java.util.Scanner;

class chessboard{
	public final int[][] a = new int[6][6];
	
	public void map() {
		int [] record = new int[36];
		int count = 0;
		int dif = 0;
		int num;
		Random ran = new Random();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				dif = 0;
				while(dif == 0) {
					a[i][j] = ran.nextInt(18)+1;
					record[count] = a[i][j];
					num = 0;
					for (int k = 0; k < count; k++)
						if (record[k] == a[i][j])
							num += 1;
					if (num < 2)
						dif = 1;
				}
				count += 1;
			}
		}
		System.out.println("original map");
		printmap(a);
	}
	
	public void printmap(int a[][]) {
		for (int i = 0; i < 6; i ++) {
			for (int j = 0; j < 6; j++) {
				System.out.printf("%4s",a[i][j]);
			}
			System.out.println(" ");
		}
		System.out.println("=====================");
	}
	
	public void playercontrol() {
		int found = 0;
		int row1, row2;
		int col1, col2;
		int road = 0;
		int road1 = 0;
		int road2 = 0;
		int road3 = 0;
		Scanner scanner = new Scanner(System.in);
		while(found == 0) {
			System.out.println("enter row1:");
			row1 = scanner.nextInt();
			System.out.println("enter col1:");
			col1 = scanner.nextInt();
			System.out.println("enter row2:");
			row2 = scanner.nextInt();
			System.out.println("enter col2:");
			col2 = scanner.nextInt();
			
			if (a[row1][col1] == a[row2][col2]) {
				if (((row1-row2==1)&&(col1==col2)) || ((row2-row1==1)&&(col1==col2))
						|| ((col1-col2==1)&&(row1==row2)) || ((col2-col1==1)&&(row1==row2))) {
					a[row1][col1] = 0; //相鄰可消
					a[row2][col2] = 0;
				}
				if ((row1==0 && row2==0) || (row1==5 && row2==5)
						|| (col1==0 && col2==0) || (col1==5 && col2==5)) {
					a[row1][col1] = 0; //邊邊可消
					a[row2][col2] = 0;
				}
				//轉1次彎的情況
				road = rowfirst(row1, row2, col1, col2);
				if (road != 0) {
					a[row1][col1] = 0;
					a[row2][col2] = 0;
				}
				else {
					road = colfirst(row1, row2, col1, col2);
					if (road != 0) {
						a[row1][col1] = 0;
						a[row2][col2] = 0;
					}
				}
				//上下轉2次彎的情況
				road1 = uptwo(row1, row2, col1, col2);
				if (road1 != 0) {
					a[row1][col1] = 0;
					a[row2][col2] = 0;
				}
				else {
					road1 = downtwo(row1, row2, col1, col2);
					if (road1 != 0) {
						a[row1][col1] = 0;
						a[row2][col2] = 0;
					}
				}
				//左右轉2次彎的情況
				road2 = lefttwo(row1, row2, col1, col2);
				if (road2 != 0) {
					a[row1][col1] = 0;
					a[row2][col2] = 0;
				}
				else {
					road2 = righttwo(row1, row2, col1, col2);
					if (road2 != 0) {
						a[row1][col1] = 0;
						a[row2][col2] = 0;
					}
				}
				//中間轉2次彎
				road3 = horizontwo(row1, row2, col1, col2);
				if (road3 != 0) {
					a[row1][col1] = 0;
					a[row2][col2] = 0;
				}
				else {
					road3 = verticaltwo(row1, row2, col1, col2);
					if (road3 != 0) {
						a[row1][col1] = 0;
						a[row2][col2] = 0;
					}
				}
			}
			found = check();
			printmap(a);
		}
		System.out.println("THE GAME IS COMPLETE");
	}
	
	public int comparecol(int row, int col1, int col2) {
		int found = 1;
		int sum = 0;
		if (col1 == col2)
			found = 1;
		else if (col1 < col2) {
			for (int col = col1+1; col < col2; col++)
				sum += a[row][col];
			if (sum != 0)
				found = 0;
		}
		else {
			for (int y = col2+1; y < col1; y++)
				sum += a[row][y];
			if (sum != 0)
				found = 0;
		}
		return found;
	}
	
	public int rowfirst(int x1, int x2, int y1, int y2) {
		int found = 1;
		int sum = 0;
		if (x1 == x2 ) {
			if (y1 < y2) {
				for (int col = y1+1; col < y2; col++)
					sum += a[x1][col];
				if (sum != 0)
					found = 0;
			}
			else {
				for (int y = y2+1; y < y1; y++)
					sum += a[x1][y];
				if (sum != 0)
					found = 0;
			}
		}
		else if (x1 < x2) {
			for (int row = x1+1; row < x2; row++)
				sum += a[row][y1];
			if (sum != 0)
				found = 0;
			else
				found = comparecol(x2, y1, y2);
		}
		else {
			for (int x = x2+1; x < x1; x++)
				sum += a[x][y2];
			if (sum != 0)
				found = 0;
			else
				found = comparecol(x1, y1, y2);
		}
		return found;
	}
	
	public int comparerow(int row1, int row2, int col) {
		int found = 1;
		int sum = 0;
		if (row1 == row2)
			found = 1;
		else if (row1 < row2) {
			for (int row = row1+1; row < row2; row++)
				sum += a[row][col];
			if (sum != 0)
				found = 0;
		}
		else {
			for (int x = row2+1; x < row1; x++)
				sum += a[x][col];
			if (sum != 0)
				found = 0;
		}
		return found;
	}
	
	public int colfirst(int x1, int x2, int y1, int y2) {
		int found = 1;
		int sum = 0;
		if (y1 == y2 ) {
			if (x1 < x2) {
				for (int row = x1+1; row < x2; row++)
					sum += a[row][y1];
				if (sum != 0)
					found = 0;
			}
			else {
				for (int x = x2+1; x < x1; x++)
					sum += a[x][y1];
				if (sum != 0)
					found = 0;
			}
		}
		else if (y1 < y2) {
			for (int col = y1+1; col < y2; col++)
				sum += a[x1][col];
			if (sum != 0)
				found = 0;
			else
				found = comparerow(x1, x2, y2);
		}
		else {
			for (int y = y2+1; y < y1; y++)
				sum += a[x2][y];
			if (sum != 0)
				found = 0;
			else
				found = comparecol(x1, y2, y1);
		}
		return found;
	}
	
	public int uptwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 0;
		int tmp2 = 0;
		int found = 1;
			for (int x1 = row1-1; x1 >= 0; x1--) {
				sum1 += a[x1][col1];
				if (sum1 != 0) {
					tmp1 = x1+1;
					break;
				}
			}
			for (int x2 = row2-1; x2 >= 0; x2--) {
				sum2 += a[x2][col2];
				if (sum2 != 0) {
					tmp2 = x2+1;
					break;
				}
			}
			if (tmp1==0 && tmp2==0) //col都=0
				found = 1;
			else if (tmp1==tmp2 && tmp1 != 0) {//等高但不=0
				if (col1 > col2)
					for (int y = col2; y <= col1; y++) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int col = col1; col <= col2; col++) {
						sum += a[tmp1][col];
						if (sum != 0)
							found = 0;
					}
			}
			else if (tmp1 > tmp2) {//tmp1比較矮
				if (col1 > col2)
					for (int y = col1; y >= col2; y--) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = col1; y <= col2; y++) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
			}
			else { //tmp2較矮
				if (col1 > col2)
					for (int y = col1; y >= col2; y--) {
						sum += a[tmp2][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = col1; y <= col2; y++) {
						sum += a[tmp2][y];
						if (sum != 0)
							found = 0;
					}
			}
	   return found;
	}
	
	public int downtwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 5;
		int tmp2 = 5;
		int found = 1;
			for (int x1 = row1+1; x1 <= 5; x1++) {
				sum1 += a[x1][col1];
				if (sum1 != 0) {
					tmp1 = x1-1;
					break;
				}
			}
			for (int x2 = row2+1; x2 <= 5; x2++) {
				sum2 += a[x2][col2];
				if (sum2 != 0) {
					tmp2 = x2-1;
					break;
				}
			}
			if (tmp1==5 && tmp2==5) //col都是空的
				found = 1;
			else if (tmp1==tmp2 && tmp1 != 5) {//等高但不=5
				if (col1 > col2)
					for (int y = col2; y <= col1; y++) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int col = col1; col <= col2; col++) {
						sum += a[tmp1][col];
						if (sum != 0)
							found = 0;
					}
			}
			else if (tmp1 < tmp2) {//tmp1比較矮
				if (col1 > col2)
					for (int y = col1; y >= col2; y--) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = col1; y <= col2; y++) {
						sum += a[tmp1][y];
						if (sum != 0)
							found = 0;
					}
			}
			else { //tmp2較矮
				if (col1 > col2)
					for (int y = col1; y >= col2; y--) {
						sum += a[tmp2][y];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = col1; y <= col2; y++) {
						sum += a[tmp2][y];
						if (sum != 0)
							found = 0;
					}
			}
	   return found;
	}
	
	public int lefttwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 0;
		int tmp2 = 0;
		int found = 1;
			for (int y1 = col1-1; y1 >= 0; y1--) {
				sum1 += a[row1][y1];
				if (sum1 != 0) {
					tmp1 = y1+1;
					break;
				}
			}
			for (int y2 = col2-1; y2 >= 0; y2--) {
				sum2 += a[row2][y2];
				if (sum2 != 0) {
					tmp2 = y2+1;
					break;
				}
			}
			if (tmp1==0 && tmp2==0) //row都=0
				found = 1;
			else if (tmp1==tmp2 && tmp1 != 0) {//等row但不=0
				if (row1 > row2)
					for (int y = row2; y <= row1; y++) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
				else
					for (int row = row1; row <= row2; row++) {
						sum += a[row][tmp1];
						if (sum != 0)
							found = 0;
					}
			}
			else if (tmp1 > tmp2) {//tmp1比較偏右
				if (row1 > row2)
					for (int y = row1; y >= row2; y--) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = row1; y <= row2; y++) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
			}
			else { //tmp2較偏右
				if (row1 > row2)
					for (int y = row1; y >= row2; y--) {
						sum += a[y][tmp2];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = row1; y <= row2; y++) {
						sum += a[y][tmp2];
						if (sum != 0)
							found = 0;
					}
			}
	   return found;
	}
	
	public int righttwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 5;
		int tmp2 = 5;
		int found = 1;
			for (int y1 = col1+1; y1 <= 5; y1++) {
				sum1 += a[row1][y1];
				if (sum1 != 0) {
					tmp1 = y1-1;
					break;
				}
			}
			for (int y2 = col2+1; y2 <= 5; y2++) {
				sum2 += a[row2][y2];
				if (sum2 != 0) {
					tmp2 = y2-1;
					break;
				}
			}
			if (tmp1==5 && tmp2==5) //row都是空的
				found = 1;
			else if (tmp1==tmp2 && tmp1 != 5) {//等高但不=5
				if (row1 > row2)
					for (int y = row2; y <= row1; y++) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
				else
					for (int row = row1; row <= row2; row++) {
						sum += a[row][tmp1];
						if (sum != 0)
							found = 0;
					}
			}
			else if (tmp1 < tmp2) {//tmp1比較偏左
				if (row1 > row2)
					for (int y = row1; y >= row2; y--) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = row1; y <= row2; y++) {
						sum += a[y][tmp1];
						if (sum != 0)
							found = 0;
					}
			}
			else { //tmp2較偏左
				if (col1 > col2)
					for (int y = row1; y >= row2; y--) {
						sum += a[y][tmp2];
						if (sum != 0)
							found = 0;
					}
				else
					for (int y = row1; y <= row2; y++) {
						sum += a[y][tmp2];
						if (sum != 0)
							found = 0;
					}
			}
	   return found;
	}
	
	public int horizontwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 7;
		int tmp2 = 8;
		int found = 1;
		if (col1 > col2) {
			for (int i = col1-1; i > col2; i--) {
				sum1 += a[row1][i];
				if (sum1 != 0) {
					tmp1 = i+1;
					break;
				}
			}
			for (int j = col2+1; j < col1; j++) {
				sum2 += a[row2][j];
				if (sum2 != 0) {
					tmp2 = j-1;
					break;
				}
			}
			if (tmp1 == tmp2) {
				for (int row = row1; row <= row2; row++)
					sum += a[row][tmp1];
				if (sum != 0)
					found = 0;
			}
			else
				found = 0;
		}
		else {
			for (int i = col2-1; i > col1; i--) {
				sum2 += a[row2][i];
				if (sum2 != 0) {
					tmp2 = i+1;
					break;
				}
			}
			for (int j = col1+1; j < col2; j++) {
				sum1 += a[row1][j];
				if (sum1 != 0) {
					tmp1 = j-1;
					break;
				}
			}
			if (tmp1 == tmp2) {
				for (int row = row2; row <= row1; row++)
					sum += a[row][tmp1];
				if (sum != 0)
					found = 0;
			}
			else
				found = 0;
		}
		return found;
	}
	
	public int verticaltwo(int row1, int row2, int col1, int col2) {
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int tmp1 = 7;
		int tmp2 = 8;
		int found = 1;
		if (row1 > row2) {
			for (int i = row1-1; i > row2; i--) {
				sum1 += a[i][col1];
				if (sum1 != 0) {
					tmp1 = i+1;
					break;
				}
			}
			for (int j = row2+1; j < row1; j++) {
				sum2 += a[j][col2];
				if (sum2 != 0) {
					tmp2 = j-1;
					break;
				}
			}
			if (tmp1 == tmp2) {
				for (int col = col1; col <= col2; col++)
					sum += a[tmp1][col];
				if (sum != 0)
					found = 0;
			}
			else
				found = 0;
		}
		else {
			for (int i = row2-1; i > row1; i--) {
				sum2 += a[i][col2];
				if (sum2 != 0) {
					tmp2 = i+1;
					break;
				}
			}
			for (int j = col1+1; j < col2; j++) {
				sum1 += a[row1][j];
				if (sum1 != 0) {
					tmp1 = j-1;
					break;
				}
			}
			if (tmp1 == tmp2) {
				for (int col = col2; col <= col1; col++)
					sum += a[tmp1][col];
				if (sum != 0)
					found = 0;
			}
			else
				found = 0;
		}
		return found;
	}
	
	public int check() {
		int sum = 0;
		int found = 0;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 6; j++)
				sum += a[i][j];
		if (sum != 0)
			found = 0;
		else
			found = 1;
		return found;	
	}
}

public class Assignment1_4105053004 {

	public static void main(String[] args) {
		chessboard x;
		x = new chessboard();
		x.map();
		x.playercontrol();
	}

}
