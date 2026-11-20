# Examen2 — Exámenes de Sangre (Arquitectura Hexagonal)

Autora: **Jhomar Alejandra Cano Ramirez**

Aplicación JavaFX para registrar exámenes de sangre, organizada con
**Arquitectura Hexagonal (Ports and Adapters)** y tres patrones de diseño:
**Strategy** (tarifas), **Adapter** (respaldo CSV) y **Factory Method** (elección del respaldo).
Cuenta con pruebas unitarias en **JUnit 5**.

## Estructura

```
co.edu.poli.Examen2
├── dominio                 Núcleo: reglas de negocio, sin dependencias externas
│   ├── modelo              ExamenSangre, CodigoExamen, NombrePaciente, Costo,
│   │                       GrupoSanguineo, TipoSangre, FactorRH
│   ├── excepcion           ReglaNegocioException, ExamenDuplicadoException
│   └── tarifa              PoliticaTarifa (Strategy) y estrategias concretas:
│                           TarifaEstandar, TarifaConDescuento, TarifaRecargoRhNegativo
├── aplicacion
│   └── casouso             Registrar / Listar / Serializar / Deserializar
│                           (implementan los puertos de entrada)
├── puertos
│   ├── entrada             Casos de uso que ofrece el sistema (driving)
│   └── salida              ExamenRepositorio, ExamenRespaldoPuerto (driven)
└── adaptadores
    ├── entrada.javafx      App (raíz de composición), PrimaryController
    └── salida
        ├── memoria         ExamenRepositorioMemoria
        ├── archivo         ExamenRespaldoArchivoAdapter, ExamenSerializableDto (.ser)
        ├── csv             ExamenRespaldoCsvAdapter (Adapter) + ClienteCsv (Adaptee) (.csv)
        └── fabrica         RespaldoFactory (Factory Method), RespaldoSerializadoFactory,
                            RespaldoCsvFactory, RespaldoFactories, FormatoRespaldo
```

**Regla de dependencia:** las flechas apuntan hacia el dominio. `dominio` no importa JavaFX ni `java.io`;
`aplicacion` solo conoce `dominio` y `puertos`; solo `App` conoce las clases concretas.

## Patrones de diseño

| Patrón | Tipo | Dónde | Para qué |
|---|---|---|---|
| **Strategy** | Comportamiento | `PoliticaTarifa` y sus implementaciones en `dominio.tarifa`; `RegistrarExamenService` actúa como *Context* | Calcular el costo final del examen sin que el caso de uso conozca la política concreta |
| **Adapter** | Estructural | `ExamenRespaldoCsvAdapter` (Adapter) adapta `ClienteCsv` (Adaptee) al puerto `ExamenRespaldoPuerto` (Target) | Reutilizar un componente CSV con API incompatible con el puerto |
| **Factory Method** | Creacional | `RespaldoFactory` (Creator) y `RespaldoCsvFactory` / `RespaldoSerializadoFactory` (Concrete Creators) | Decidir qué adaptador de respaldo se instancia sin acoplar a quien lo usa |

## Reglas de negocio

- Código: formato `XXX-000` (ej. `EXM-001`), único. Se normaliza a mayúsculas y sin espacios.
- Nombre del paciente: obligatorio, máximo 100 caracteres.
- Costo: mayor que cero (se guarda con 2 decimales, redondeo `HALF_UP`).
- Tipo de sangre (A, B, AB, O) y factor Rh obligatorios.
- Costo final según la política de tarifa activa:
  - `TarifaEstandar`: sin cambios.
  - `TarifaConDescuento(p)`: descuento de `p` % (0 < p < 100) a todos los exámenes.
  - `TarifaRecargoRhNegativo(p)`: recargo de `p` % solo a pacientes Rh negativo.
  - Por defecto `App` usa `TarifaRecargoRhNegativo` con 15 %.

## Ejecución

```
mvn clean javafx:run
```

Requiere JDK 11+ y Maven.

### Formato del respaldo

El botón «Serializar» escribe `examenes.<ext>` en el directorio de ejecución y «Deserializar» lo restaura.
El formato se elige con la propiedad de sistema `respaldo.formato`:

| Valor | Archivo | Adaptador |
|---|---|---|
| `serializado` (por defecto) | `examenes.ser` | `ExamenRespaldoArchivoAdapter` |
| `csv` | `examenes.csv` | `ExamenRespaldoCsvAdapter` |

Ejemplo: agregar `-Drespaldo.formato=csv` en los *VM arguments* de la configuración de ejecución
(por ejemplo, en Eclipse: *Run Configurations → Arguments → VM arguments*).
Un valor desconocido produce un `IllegalArgumentException` al iniciar.

Seguridad de la deserialización: el adaptador `.ser` usa un filtro (`ObjectInputFilter`) con lista blanca de clases,
y ambos adaptadores reconstruyen el dominio a través de sus fábricas, por lo que un archivo manipulado con datos
inválidos es rechazado.

## Pruebas unitarias (JUnit 5)

```
mvn test
```

Las pruebas están en `src/test/java` y reflejan la estructura de paquetes del código:

| Paquete | Clases de prueba | Qué verifican |
|---|---|---|
| `dominio.modelo` | `CodigoExamenTest`, `NombrePacienteTest`, `CostoTest`, `GrupoSanguineoTest`, `ExamenSangreTest` | Validaciones, normalización, inmutabilidad, igualdad e identidad |
| `dominio.tarifa` | `TarifaEstandarTest`, `TarifaConDescuentoTest`, `TarifaRecargoRhNegativoTest` | Cálculo de cada estrategia y validación de sus parámetros |
| `aplicacion.casouso` | `RegistrarExamenServiceTest`, `ListarExamenesServiceTest`, `SerializarExamenesServiceTest`, `DeserializarExamenesServiceTest` | Flujo de cada caso de uso, duplicados, delegación en la estrategia y propagación de errores del puerto |
| `adaptadores.salida.memoria` | `ExamenRepositorioMemoriaTest` | Orden de inserción, sobrescritura, lista de solo lectura, reemplazo |
| `adaptadores.salida.archivo` | `ExamenRespaldoArchivoAdapterTest` | Ida y vuelta, archivo inexistente/corrupto, filtro de clases |
| `adaptadores.salida.csv` | `ClienteCsvTest`, `ExamenRespaldoCsvAdapterTest` | Comillas, separadores, saltos de línea, encabezado y filas inválidas |
| `adaptadores.salida.fabrica` | `FormatoRespaldoTest`, `RespaldoFactoriesTest` | Selección de formato y de fábrica, propiedad `respaldo.formato` |
| `soporte` | `Fixtures`, `RespaldoFalso` | Utilidades y doble de prueba del puerto de respaldo (no son pruebas) |

Los adaptadores de archivo usan `@TempDir`, así que las pruebas no dejan archivos en el proyecto.
No se prueban `App` ni `PrimaryController` porque dependen de JavaFX; toda la lógica relevante vive en el
dominio y la aplicación, que sí están cubiertos.

## Documentación y Artefactos (Wiki)

Los entregables complementarios del proyecto se encuentran alojados en la [Wiki del repositorio](https://github.com/jhomarcano/Software2Examen3/wiki) y en la carpeta `Examen2/docs`:

- **Actividad 1 (Diagnóstico inicial):** [actividad1.pdf](https://github.com/jhomarcano/Software2Examen3/blob/main/Examen2/docs/actividad1.pdf)
- **Actividad 2 (Arquitectura Hexagonal):** Diagramas `dominio2` y `arquitectura2` en la Wiki.
- **Actividad 3 (Patrones GoF):** [actividad3.pdf](https://github.com/jhomarcano/Software2Examen3/blob/main/Examen2/docs/actividad3.pdf) y diagrama `dominio3` en la Wiki.
- **Actividad 4 (Calidad de código):** Evidencia de análisis estático con **SonarQube** en la Wiki.
- **Actividad 5 (Sustentación):** Video `video5` enlazado en la Wiki.

## Declaración del uso de Inteligencia Artificial

Durante el desarrollo del proyecto se emplearon herramientas de IA como apoyo puntual en las siguientes actividades:

- **Claude:** apoyo en la generación y refactorización del código en Java (estructura hexagonal, patrones GoF, comentarios JavaDoc y pruebas unitarias en JUnit 5).
- **Gemini:** apoyo en la depuración y resolución de errores de configuración (Maven/JavaFX) y en la revisión de la redacción de los informes (`actividad1.pdf` y `actividad3.pdf`).
