
#include "asoc_io.h"

void escribirDiccionario(NodoDiccionario * nodo, FILE * fichero){
	NodoDiccionario * it = nodo;
	while(it != NULL){
		char cad[50*2];
		strcpy(cad, it->clave);
		strcat(cad, "-");
		strcat(cad, it->valor);
		strcat(cad, "-\n");
		fputs(cad, fichero);
		it = it->sig;
	}
}

void guardarLista(NodoLista * nodo){
	FILE * fichero = NULL;
	fichero = fopen("lista.asoc", "wt");
	if(fichero != NULL){
		NodoLista * it = nodo;
		while(it != NULL){
			char cad[50];
			char id[20];
			sprintf(id, "%d", it->id);
			
			strcpy(cad, "#lista\n");
			strcat(cad, id);
			strcat(cad, "\n#diccionario\n");
			int res = fputs(cad, fichero);
			if(res != EOF){
				escribirDiccionario(it->diccionario, fichero);
				fputs("\n", fichero);
			}
			it = it->sig;
		}
		fclose(fichero);
	}
}

void recuperarLista(){
	FILE * fichero = NULL;
	fichero = fopen("lista.asoc", "rt");
	if(fichero != NULL){
		char cad[50*2];
		char * leida;
		char strLista[7] = "#lista";
		char strDicc[13] = "#diccionario";
		char separador[2] = "-";
		char * clave = NULL;
		char * valor = NULL;
		int id = 0;
		int paso = 0;
		int idLeida = 0;
		//paso 0 = leer #lista o las asociaciones de un diccionario
		//paso 1 = leer id
		//paso 2 = leer #diccionario
		leida = fgets(cad, 50*2, fichero);
		//fgetc(fichero);
		while(leida != NULL){
			if(strncmp(leida, strLista, 6) == 0){
				paso = 1;
			}else if(paso == 1){
				id = atoi(leida);
				paso = 2;
			}else if(paso == 2 && strncmp(leida, strDicc, 12)==0){
				paso = 3;
			}else if(paso == 3 && strlen(leida)>1){
				clave = strtok(leida,separador);
				valor = strtok(NULL,separador);
				ponerasociacion_1_svc(id, clave, valor, NULL);
			}
			leida = fgets(cad, 50*2, fichero);
		}
		fclose(fichero);
	}
}
