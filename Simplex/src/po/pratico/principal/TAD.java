package po.pratico.principal;

public class TAD {
	
	public static int Iteracao = 0;
	public double objetivo = 0; //resultado da funcao objetivo
	
	public double[][] matrizA = {  //x1, x2, x3, x4, x5, x6, x7, x8, x9,x10,x11,x12,x13,x14,x15, y1, y2, y3
									{ 1,  9,  1, 10,  4,  2, 10,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0}, 
									{ 1,  0,  0,  0,  0,  0,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0},
									{ 0,  1,  0,  0,  0,  0,  0,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  1},
									{ 0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0},
									{ 0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0},
									{ 0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0},
									{ 0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0},
									{ 0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0},
									{ 0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0},
								}; //tamanho mxn
	public double[][] b = {{10944}, {5472}, {120}, {430}, {30}, {70}, {20}, {30}, {25}}; //tamanho m
	public double[][] c = {{-1.05, -19.183, -2.04, -23.983, -13.6, -8.13, -17.82, 0, 0, 0, 0, 0, 0, 0, 0, 1000, 1000, 1000}}; //tamanho n
	public int m;
	public int n;
	public double[][] matrizB; //dimensao mxm
	public double[][] matrizBInversa;
	public double[][] x_basico; //vetor solucao x_b (apenas dos x's que estao na base)
	public int[] indicesBase = {9, 10, 11, 12, 13, 14, 15, 16, 17}; //tamanho m (posicao das colunas base)
	public int[] indicesNaoBase = {0, 1, 2, 3, 4, 5, 6, 7, 8}; //tamanho n - m; (posicao das colunas não base)
	public double[][] CustosBase; //tamanho m
	public int JotaEscolhido; //coluna escolhida para entrar na base (indice)
	public double CustoEscolhido; //custo reduzido do J escolhido 
	public double Theta; //inicialmente deve ser um valor muito alto
	public int IndiceSaiDaBase;//coluna escolhida para sair da base (indice)
	public double razao; //variavel para comparar com o theta no cálculo de qual é o menor theta
	public double[] d_b; //direção factivel da variavel nao basica escolhida para entrar na base
	public double[] solucao; //vetor solucao tamanho n
	
	public TAD(){
		m = b.length;
		n = c[0].length;
		matrizB = MatrizCria(m,m);
		matrizBInversa = MatrizCria(m,m);
		x_basico = MatrizCria(m,1);
		CustosBase = MatrizCria(1,m);
		d_b = VetorCria(m);
		solucao = VetorCria(n);
	}
	
	
	/*================INICIO OPERAÇÕES MATRICIAIS================================*/
	
	
	
	 double[][] MatrizCria(int linha, int coluna) {
		// Prerequisitos (lin e col devem ser numeros positivos nao nulos):
		if ((linha <= 0) || (coluna <= 0))
			return null;

		// Aloca memoria para uma matriz e define seu tamanho:
		double[][] nova = new double[linha][coluna];

		// Inicializa a matriz com 0's:
		for (int i = 0; i < linha; i++) {
			for (int j = 0; j < coluna; j++) {
				nova[i][j] = 0;
			}
		}

		return nova;
	}
	
	 double[] VetorCria(int linha) {
		if (linha <= 0)
			return null;

		// Aloca memoria para um vetor e define seu tamanho:
		double[] nova = new double[linha];

		// Inicializa a matriz com 0's:
		for (int i = 0; i < linha; i++) {
				nova[i]= 0;
		}

		return nova;
	}
	
	
	public void printaMatriz(double[][] matriz) {

		if (matriz == null) {
			return;
		}

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				System.out.printf("%.4f\t",matriz[i][j]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public void printaVetor(double[] vetor) {

		if (vetor == null) {
			return;
		}

		for (int i = 0; i < vetor.length; i++) {
		
			System.out.printf("%.4f\t",vetor[i]);
		}
		System.out.println("");
	}
	
	public void printaVetorInteger(int[] vetor) {

		if (vetor == null) {
			return;
		}

		for (int i = 0; i < vetor.length; i++) {
		
			System.out.printf("%d\t",vetor[i]);
		}
		System.out.println("");
	}
	
	public double[] VetorNegativo(double[] vetor){
		double[] aux = new double[vetor.length];
		
		for (int i = 0; i < vetor.length; i++) {
			if(vetor[i] != 0)
				aux[i] = vetor[i] * -1;
			
		}
		return aux;
	}
	

	private void MatrizTransformaLinha(double[][] matriz, int linhaAlvo, int linha, double escalar) {
		// Prerequisitos (Matriz nao nula, linhas validas):
		if ((matriz == null) || (linhaAlvo < 0) || (linhaAlvo >= matriz.length) || (linha < 0)
				|| (linha >= matriz.length))
			return;

		for (int i = 0; i < matriz.length; i++)
			matriz[linhaAlvo][i] = matriz[linhaAlvo][i] - (matriz[linha][i] * escalar);

		return;
	}

	public double VetorProdutoEscalar(double[] vetorA, double[] vetorB) {
		if((vetorA == null) || (vetorB == null))
	        return -1;
		
		double result = 0;

		for (int i = 0; i < vetorA.length; i++) {
			result = vetorA[i] * vetorB[i] + result;
		}

		return result;
	}

	public double[][] MatrizProduto(double[][] matrizA, double[][] matrizB) {
		double[][] result = new double[matrizA.length][matrizB[0].length];

		// Número de linhas da Matriz A
		for (int i = 0; i < matrizA.length; i++) {
			// Número de colunas da Matriz B
			for (int j = 0; j < matrizB[0].length; j++) {
				double valor = 0;
				// Número de coluna da Matriz A
				for (int k = 0; k < matrizA[0].length; k++) {

					valor = valor + matrizA[i][k] * matrizB[k][j];

				}
				result[i][j] = valor;
			}

		}

		return result;
	}
	public double[] MatrizVetorProduto(double[][] matrizA, double[] vetor) {
		double[] result = new double[matrizA.length];

		// Número de linhas da Matriz A
		for (int i = 0; i < matrizA.length; i++) {
				double valor = 0;
				// Número de coluna da Matriz A
				for (int k = 0; k < matrizA[0].length; k++) {

					valor = valor + matrizA[i][k] * vetor[k];

				}
				result[i] = valor;

		}

		return result;
	}

	public double[][] MatrizTransposta(double[][] matriz) {
		
		if(matriz == null){
	        return null;
		}
	        
		double[][] transposta = new double[matriz[0].length][matriz.length];

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				transposta[j][i] = matriz[i][j];
			}
		}

		return transposta;
	}

	public double[][] MatrizInversa(double[][] matriz) {
		// Prerequisitos (Matriz quadrada nao nula):
		if ((matriz == null) || (matriz.length != matriz[0].length))
			return null;

		// Cria matriz identidade para ser usada como conjunto solucao:
		double[][] identidade = MatrizIdentidade(matriz.length);

		// Cria matriz que armazenara a inversa:
		double[][] inversa = MatrizCria(matriz.length, matriz.length);

		// Determina as matrizes: lower, upper, e permutacao:
		double[][] P = MatrizIdentidade(matriz.length);
		double[][] upper = MatrizCopia(matriz);
		double[][] lower = MatrizDecomposicaoPivotLU(upper, P);

		// Resolve n sistemas, para cada coluna de identidade:
		double[][] b = MatrizCria(matriz.length, 1);
		for (int i = 0; i < matriz[0].length; i++) {
			// Resolva o sistema para a coluna atual da identidade:
			for (int k = 0; k < matriz.length; k++)
				b[k][0] = identidade[k][i];

			double[][] colInversa = MatrizSolucaoPivotLU(upper, lower, P, b);

			// Copia o resultado do sistema linear para a matriz inversa:
			for (int j = 0; j < inversa.length; j++)
				inversa[j][i] = colInversa[0][j];

		}

		return inversa;
	}

	private double[][] MatrizSolucaoPivotLU(double[][] upper, double[][] lower, double[][] P, double[][] b) {
		// Prerequisitos (Matrizes quadradas nao nulas, vetor do tamanho da
		// matriz):
		if ((upper == null) || (lower == null) || (P == null) || (upper.length != upper[0].length)
				|| (lower.length != lower[0].length) || (lower.length != upper.length) || (P.length != P[0].length)
				|| (P.length != upper.length))
			return null;

		// Realiza o produto matricial entre a matriz de permutacoes e a matriz
		// solucao:
		double[][] Pb = MatrizProdutoMatricial(P, b);

		// Substituicoes sucessivas:
		double[][] aux = MatrizSubstSucessiva(lower, Pb);

		// Substituicoes retroativas:
		double[][] res = MatrizSubstRetroativa(upper, aux);

		// Retorna a matriz solucao:
		return res;
	}

	private double[][] MatrizProdutoMatricial(double[][] matriz1, double[][] matriz2) {
		// Prerequisitos (Matrizes nao nulas, n.o de colunas de 'matriz1' igual ao
		// n.o de linhas de 'matriz2'):
		if ((matriz1 == null) || (matriz2 == null) || (matriz1[0].length != matriz2.length))
			return null;

		// Cria nova matriz:
		double[][] mult = MatrizCria(matriz1.length, matriz2[0].length);

		// Faz a multiplicacao:
		for (int i = 0; i < matriz1.length; i++) {
			for (int k = 0; k < matriz2[0].length; k++) {
				for (int j = 0; j < matriz1[0].length; j++) {
					mult[i][k] = mult[i][k] + (matriz1[i][j] * matriz2[j][k]);
				}
			}
		}
		return mult;
	}
	
	
	private double[][] MatrizSubstSucessiva(double[][] lower, double[][] b){
	    // Prerequisitos (Matriz quadrada nao nula, vetor do tamanho da matriz):
	    if((lower == null) || (lower.length != lower[0].length))
	        return null;
	    
	    // Cria uma matriz (1, n) auxiliar para armazenar 'y' em (Ly = b):
	    double[][] aux = MatrizCria(1, lower[0].length);

	    // Primeira linha e usada como padrao:
	    aux[0][0] = b[0][0] / lower[0][0];

	    // Itera sobre cada linha (1 variavel por linha):
	    for(int i = 1; i < lower.length; i++){
	        // Soma cada variavel conhecida com seu coeficiente:
	        double soma = 0;
	        for(int j = 0; j < i; j++){
	            soma = soma + lower[i][j] * aux[0][j];
	        }
	        // Resolve a igualdade e armazena:
	        aux[0][i] = (b[i][0] - soma) / lower[i][i];
	    }

	    return aux;
	}
	
	private double[][] MatrizSubstRetroativa(double[][] upper, double[][] y){
	    // Prerequisitos (Matriz quadrada nao nula, vetor(1, n)):
	    if((upper == null) || (upper.length != upper[0].length) || (y.length != 1) || (y[0].length != upper[0].length))
	        return null;

	    // Cria uma matriz (1, n) para armazenar 'x' em (Ux = y):
	    double[][] res = MatrizCria(1, upper.length);

	    // Ultima linha e usada como padrao:
	    res[0][y[0].length - 1] = y[0][y[0].length - 1] / upper[upper[0].length - 1][upper[0].length - 1];

	    // Itera sobre cada linha, de baixo para cima:
	    for(int i = upper[0].length - 2; i >= 0; i--){
	        // Soma cada variavel conhecida com seu coeficiente:
	        double soma = 0;
	        for(int j = i; j < upper[0].length; j++){
	            soma = soma + upper[i][j] * res[0][j];
	        }
	        // Resolve a igualdade e armazena:
	        res[0][i] = (y[0][i] - soma) / upper[i][i];
	    }

	    return res;
	}

	public double[][] MatrizDecomposicaoPivotLU(double[][] upper, double[][] P) {
		// Prerequisitos (Matriz quadrada nao nula):
		if ((upper == null) || (P == null) || (upper.length != upper[0].length) || (P.length != upper.length)
				|| (P[0].length != upper[0].length))
			return null;

		// Cria uma nova matriz para receber os fatores:
		double[][] lower = MatrizCria(upper.length, upper.length);

		// Decompoe:
		for (int i = 0; i < upper.length - 1; i++) {
			// Localiza o pivo:
			int pivo = MatrizLocalizaPivo(upper, i, i);

			// Troca de linhas se necessario:
			if (pivo != i) {
				MatrizTrocaLinhas(upper, pivo, i);
				MatrizTrocaLinhas(lower, pivo, i);
				MatrizTrocaLinhas(P, pivo, i);
			}

			// Eliminacao de gauss (se elemento na linha atual nao for zero):
			if (Math.abs(upper[i][i]) != 0) {
				for (int j = i + 1; j < upper.length; j++) {
					// Determina o multiplicador:
					double mult = upper[j][i] / upper[i][i];

					// Salva o multiplicador em 'lower':
					lower[j][i] = mult;

					// Multiplica a linha:
					MatrizTransformaLinha(upper, j, i, mult);
				}
			}
		}

		// Coloca 1's na diagonal principal de 'lower':
		for (int i = 0; i < lower.length; i++)
			lower[i][i] = 1;

		// Retorna a triangular inferior:
		return lower;
	}

	public int MatrizLocalizaPivo(double[][] matriz, int linha, int coluna) {
		// Prerequisitos (Matriz nao nula, linha e coluna validos):
		if ((matriz == null) || (linha < 0) || (linha >= matriz.length) || (coluna < 0) || (coluna >= matriz[0].length))
			return -1;

		int pivo = linha; // A linha inicial sera o pivo.
		double maior = Math.abs(matriz[linha][coluna]);

		// Percorre as demais linhas procurando um valor maior:
		for (int i = linha + 1; i < matriz.length; i++)
			if (Math.abs(matriz[i][coluna]) >= maior) {
				maior = Math.abs(matriz[i][coluna]); // Atualiza o valor do
														// maior
				// elemento.
				pivo = i; // Atualiza a posicao da linha com o pivo.
			}

		return pivo;
	}

	private void MatrizTrocaLinhas(double[][] matriz, int lin1, int lin2) {
		// Prerequisitos (Matriz nao nula, linhas validas):
		if ((matriz == null) || (lin1 < 0) || (lin1 >= matriz.length) || (lin2 < 0) || (lin2 >= matriz.length))
			return;

		// Faz a troca:
		double[] aux;
		aux = matriz[lin1];
		matriz[lin1] = matriz[lin2];
		matriz[lin2] = aux;

		return;
	}

	private double[][] MatrizIdentidade(int tamanho) {
		double[][] identidade = new double[tamanho][tamanho];

		if (tamanho <= 0) {
			return null;
		}

		for (int i = 0; i < identidade.length; i++) {
			identidade[i][i] = 1;
		}

		return identidade;
	}

	public double[][] MatrizCopia(double[][] matriz) {
		// Prerequisitos (Matriz nao nula):
		if (matriz == null)
			return null;

		// Cria uma nova matriz:
		double[][] copia = new double[matriz.length][matriz.length];

		// Copia cada elemento de matriz original:
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				copia[i][j] = matriz[i][j];
			}
		}

		return copia;
	}

	
	/*================FIM OPERAÇÕES MATRICIAIS================================*/

}
