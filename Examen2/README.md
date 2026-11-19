# Examen2 — Exámenes de Sangre (Arquitectura Hexagonal)

Autora: **Jhomar Alejandra Cano Ramirez**

Refactorización del proyecto original (JavaFX con controlador vacío) hacia
**Arquitectura Hexagonal (Ports and Adapters)**.

## Estructura

```
co.edu.poli.Examen2
├── dominio            Entidades, objetos de valor y reglas (sin dependencias externas)
│   ├── modelo         ExamenSangre, CodigoExamen, NombrePaciente, Costo, GrupoSanguineo, TipoSangre, FactorRH
│   └── excepcion      ReglaNegocioException, ExamenDuplicadoException
├── aplicacion
│   └── casouso        Registrar / Listar / Serializar / Deserializar (implementan los puertos de entrada)
├── puertos
│   ├── entrada        Casos de uso que el sistema ofrece (driving)
│   └── salida         ExamenRepositorio, ExamenRespaldoPuerto (driven)
└── adaptadores
    ├── entrada.javafx App (raíz de composición), PrimaryController
    └── salida
        ├── memoria    ExamenRepositorioMemoria
        └── archivo    ExamenRespaldoArchivoAdapter, ExamenSerializableDto
```

**Regla de dependencia:** las flechas apuntan hacia el dominio. `dominio` no importa JavaFX ni `java.io`;
`aplicacion` solo conoce `dominio` y `puertos`; solo `App` conoce las clases concretas.

## Reglas de negocio

- Código: formato `XXX-000` (ej. `EXM-001`), único.
- Nombre del paciente: obligatorio, máximo 100 caracteres.
- Costo: mayor que cero (se guarda con 2 decimales).
- Tipo de sangre (A, B, AB, O) y factor Rh obligatorios.

## Ejecución

```
mvn clean javafx:run
```

Requiere JDK 11+ y Maven. «Serializar» escribe `examenes.ser` en el directorio de ejecución.

## Diagramas

En `docs/`: `dominio2.mmd` (dominio) y `arquitectura2.mmd` (arquitectura), en formato Mermaid.
Se pueden renderizar en https://mermaid.live.
