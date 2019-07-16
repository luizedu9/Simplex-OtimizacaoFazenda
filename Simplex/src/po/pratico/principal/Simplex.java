package po.pratico.principal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Simplex {
	
	public Simplex(TAD dados){
		
		System.out.println("\n============  AUMENTO DO LUCRO DE FAZENDA ==============\n");

		System.out.println("Matriz A: \n");
		dados.printaMatriz(dados.matrizA);
		System.out.println("Vetor b: \n");
		dados.printaMatriz(dados.b);
		System.out.println("Vetor de Custos: \n ");
		dados.printaMatriz(dados.c);
		
		
		do{
			
			/**====================================================================================*/
			/**PASSO 1: CALCULANDO SBF INICIAL*/
			/**====================================================================================*/
			
			System.out.println("\n========================================================== ");
			System.out.println("Interação: " + TAD.Iteracao);
			System.out.println("==========================================================\n ");
			
			
			System.out.println("\nIndices Basicos: \n");
			dados.printaVetorInteger(dados.indicesBase);
			System.out.println("\nIndices Nao-Basicos: \n");
			dados.printaVetorInteger(dados.indicesNaoBase);
			
			
			/*copia as colunas base de A para B*/
			for (int i=0; i < dados.m; i++){
				for (int j=0; j < dados.n; j++){
					if (dados.indicesBase[i]==j){	
						for (int k=0; k < dados.m; k++){
							dados.matrizB[i][k] = dados.matrizA[k][j];	
						}
					}
				}
				
			}
			dados.matrizB = dados.MatrizTransposta(dados.matrizB);
			System.out.println("\nMatriz B: \n");
			dados.printaMatriz(dados.matrizB);
			
			

			/*calcula SBF inicial pelo produto da inversa de B com b*/
			dados.matrizBInversa = dados.MatrizInversa(dados.matrizB);
			dados.x_basico = dados.MatrizProduto(dados.matrizBInversa, dados.b);
			
			/*exibe matriz inversa de bB*/
			System.out.println("\nMatriz B Inversa: \n");
			dados.printaMatriz(dados.matrizBInversa);
			
			/*exibe solucao basica*/
			System.out.println("Solução básica factivel da Interacao "+TAD.Iteracao +": \n");
			dados.printaMatriz(dados.x_basico);
			
			dados.objetivo = 0;
			for(int i=0; i < dados.m; i++)
			{
				dados.objetivo =  dados.objetivo + (dados.c[0][dados.indicesBase[i]] * dados.x_basico[i][0]);
			}
			
			/*exibe objetivo */
			System.out.println("\nObjetivo: " +  dados.objetivo);
			
			
			
			/**====================================================================================*/
			/**PASSO 2: CALCULANDO OS CUSTOS REDUZIDOS DOS INDICES NAO BASICOS === REQUISITO 04*/
			/**====================================================================================*/
		
			for(int i=0; i < dados.m; i++)
			{
				dados.CustosBase[0][i] = dados.c[0][dados.indicesBase[i]];
			}
			
			System.out.println("\nCusto Basico:\n");
			dados.printaMatriz(dados.CustosBase);
			
			/*Escolher indice nao basico de custo reduzido mais negativo*/
			dados.JotaEscolhido = -1;
			dados.CustoEscolhido = 1000000;
			
			for(int j=0;j<dados.indicesNaoBase.length;j++)
			{
				
				//System.out.println("\nColuna Aj (A"+dados.indicesNaoBase[j]+"):\n");
				double[][] A_Transposta = dados.MatrizTransposta(dados.matrizA);
				double[] Aj = new double[dados.m];
				Aj = A_Transposta[dados.indicesNaoBase[j]];
				//dados.printaVetor(Aj);
				
				/*Calcula a j-esima direcao factivel pelo produto -B^{-1}A_j*/
				double[]Direcao = new double[dados.m];
						Direcao = dados.MatrizVetorProduto(dados.matrizBInversa, Aj);
						//System.out.println("\nDirecao d("+dados.indicesNaoBase[j]+"):\n");
						//dados.printaVetor(Direcao);
						
						
						
				/*Calcula o custo reduzido*/			
				double custo = dados.c[0][dados.indicesNaoBase[j]] - dados.VetorProdutoEscalar(dados.CustosBase[0],Direcao);
				System.out.println("\nCusto "+dados.indicesNaoBase[j]+": "+custo+"\n");
				
				if(custo < 0)
				{
					if(custo < dados.CustoEscolhido)
					{
						dados.JotaEscolhido  = j;
						dados.CustoEscolhido = custo;
					}
				}
				
			}
			/*Se não encontrar nenhum custo negativo, entra nesse if. Significa que achou solucao otima.*/
			if(dados.JotaEscolhido == -1)
			{
				
				dados.objetivo =0;
				for (int i=0; i < dados.m; i++)
				{
					dados.objetivo = dados.objetivo + (dados.CustosBase[0][i] * dados.x_basico[i][0]);
				}
				dados.solucao = dados.VetorCria(dados.n); //zera vetor
				for (int i=0; i < dados.m; i++)
				{ 
					dados.solucao[dados.indicesBase[i]] = dados.x_basico[i][0];
				}
				System.out.println("\n-------------------------------------------------------------------");
				//System.out.println("\nObjetivo = " + (dados.objetivo * -1) + "(encontrado na " + TAD.Iteracao + "a. iteracao)\n\n");
				System.out.printf("Lucro = R$ %.2f (encontrado na %da iteração) \n", (dados.objetivo * -1), TAD.Iteracao);
				System.out.printf("Leite = %.0f\n", dados.solucao[0]);
				System.out.printf("Queijo = %.0f\n", dados.solucao[1]);
				System.out.printf("Iogurte = %.0f\n", dados.solucao[2]);
				System.out.printf("Muçarela = %.0f\n", dados.solucao[3]);
				System.out.printf("Doce de Leite = %.0f\n", dados.solucao[4]);
				System.out.printf("Leite Condensado = %.0f\n", dados.solucao[5]);
				System.out.printf("Requeijão = %.0f\n", dados.solucao[6]);
				System.out.printf("Manteiga = %.1f", dados.solucao[1] * 0.05);
				System.out.println("\n-------------------------------------------------------------------");
				
				
				FileWriter arq;
				try {
					arq = new FileWriter("Resultado.txt");
					PrintWriter gravarArq = new PrintWriter(arq);					
					gravarArq.printf("Vender %.0f L de Leite\n", dados.solucao[0]);
					gravarArq.printf("Produzir %.0f KG de Queijo\n", dados.solucao[1]);
					gravarArq.printf("Com o queijo produzido, é possivel gerar %.1f KG de Manteiga\n", dados.solucao[1] * 0.05);
					gravarArq.printf("Produzir %.0f L de Iogurte\n", dados.solucao[2]);
					gravarArq.printf("Produzir %.0f KG de Muçarela\n", dados.solucao[3]);
					gravarArq.printf("Produzir %.0f KG de Doce de Leite\n", dados.solucao[4]);
					gravarArq.printf("Produzir %.0f KG de Leite Condensado\n", dados.solucao[5]);
					gravarArq.printf("Produzir %.0f KG de Requeijão\n", dados.solucao[6]);		
					gravarArq.printf("Lucro Final = R$ %.2f\n", (dados.objetivo * -1));
				    arq.close();
				} catch (IOException e) {
				}
			    
				
				/*dados.solucao = dados.VetorCria(dados.n); //zera vetor
				for (int i=0; i < dados.m; i++)
				{ 
					dados.solucao[dados.indicesBase[i]] = dados.x_basico[i][0];
				}
				for (int i=0; i < dados.n; i++)
				{
					System.out.println("x[" + i + "] = " + dados.solucao[i] + "\n");
				}*/

				return;
			}
			
			/*Se não encontrou a solução, continua aqui*/
			/*Exibe quem entra na base*/
			System.out.println("\nVariavel Entra Base: x["  + dados.JotaEscolhido + "]\n");
			

			/**====================================================================================*/
			/**PASSO 3: COMPUTA VETOR U ----- REQUISITO 05: CALCULO DE d_b = -B^(-1)*Aj*/ 
			/**====================================================================================*/
			

			/*verificar se a situacao nao eh ilimitada*/
			double[] u = new double[dados.m];
			
			double[][] A_Transposta = dados.MatrizTransposta(dados.matrizA);
			double[] Aj = new double[dados.m];
			Aj = A_Transposta[dados.JotaEscolhido];
		
			u = dados.MatrizVetorProduto(dados.matrizBInversa,Aj);
			
			/*Verifica se nenhum componente de u eh positivo*/
			boolean ExistePositivo = false;
			for(int i=0; i < dados.m; i++)
			{
				if(u[i] > 0)
				{
					ExistePositivo = true;
				}		
			}

			/*se nao houver no vetor 'u' (sinal inverso da direcao factivel) nenhum componente
			positivo, entao porque o valor otimo eh - infinito.*/
			if(ExistePositivo==false)
			{
				System.out.println("\n\nCusto Otimo = -Infinito");
				return;
			}

			/**====================================================================================*/
			/**PASSO 4: CALCULA O VALOR DE THETA ----- REQUISITO 06 
			/**====================================================================================*/

			/*Chuta um valor alto para o theta, e vai reduzindo de acordo com a razao x_i / u_i*/
			dados.Theta = 10000;
			dados.IndiceSaiDaBase = -1;

			/*Varre indices basicos determinando o valor de theta que garante factibilidade*/
			for(int i=0; i < dados.m;i++)
			{
				//Calcula a razao
				if(u[i] > 0)
				{
					//Calcula a razao
					dados.razao = dados.x_basico[i][0] / u[i];

					/*Atualiza a razao, pois encontramos um menor valor de theta*/
					if(dados.razao < dados.Theta)
					{
						dados.Theta  = dados.razao;
						dados.IndiceSaiDaBase = dados.indicesBase[i];
					}
				}
			}

			/*Exibe variavel que ira deixar a base*/
			System.out.println("\nVariavel  Sai  Base: x[" + dados.IndiceSaiDaBase + "], Theta = " + dados.Theta + "\n");

			/*====================================================================================*/
			/*PASSO 5: ATUALIZA VARIAVEL BASICA E NAO BASICA ---- REQUISITO 07: MUDANCA DE BASE*/
			/*====================================================================================*/

			/*Calcula novo valor da nao-basica, e atualiza base*/
			for(int i=0; i < dados.m;i++)
			{
				if(dados.indicesBase[i] == dados.IndiceSaiDaBase)
				{
					dados.x_basico[i][0] = dados.Theta;
					dados.indicesBase[i] = dados.JotaEscolhido;
				}
			}

			for(int i=0; i < dados.n - dados.m; i++)
			{
				if(dados.indicesNaoBase[i] == dados.JotaEscolhido)
				{
					dados.indicesNaoBase[i] = dados.IndiceSaiDaBase;
				}
			}

		/*Incrementa o numero da iteracao*/
		TAD.Iteracao = TAD.Iteracao + 1;	
			
	 }while (true);
		
	}
	
	public static void main(String[] args) {
		TAD dados = new TAD();
		new Simplex(dados);
	}

		

}
