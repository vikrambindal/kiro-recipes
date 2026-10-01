package com.kirotodo.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Enforces the Hexagonal Architecture (Ports and Adapters) dependency rules
 * as defined in .kiro/steering/coding-standards/hexagonal-architecture.md.
 *
 * Dependency rules:
 *   adapter/in  → port/in only          (never core/domain or adapter/out)
 *   core/domain → port/out only         (never adapter/in or adapter/out)
 *   adapter/out → port/out + entity     (never core/domain or adapter/in)
 *   port/*      → no internal deps      (pure Java — no Spring, JPA, or adapter imports)
 */
@AnalyzeClasses(
    packages = "com.kirotodo",
    importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalArchitectureTest {

    private static final String ROOT              = "com.kirotodo";
    private static final String ADAPTER_IN        = ROOT + ".adapter.in..";
    private static final String ADAPTER_OUT       = ROOT + ".adapter.out..";
    private static final String CORE_DOMAIN       = ROOT + ".core.domain..";
    private static final String PORT_IN           = ROOT + ".port.in..";
    private static final String PORT_OUT          = ROOT + ".port.out..";
    private static final String CONFIGURATION     = ROOT + ".configuration..";

    // ─────────────────────────────────────────────────────────────────────
    // Layer dependency rules  (hexagonal-architecture.md — Dependency Rules)
    // ─────────────────────────────────────────────────────────────────────

    /**
     * adapter/in must only call port/in interfaces — never core/domain services directly.
     * (adapter-layer.md: "Inject and call port/in interfaces only")
     */
    @ArchTest
    static final ArchRule incomingAdaptersMustOnlyDependOnInboundPorts =
        noClasses().that().resideInAPackage(ADAPTER_IN)
            .should().dependOnClassesThat().resideInAPackage(CORE_DOMAIN)
            .because("adapter/in must call port/in interfaces — never core/domain directly");

    /**
     * adapter/in must not access outgoing adapters or the database layer.
     */
    @ArchTest
    static final ArchRule incomingAdaptersMustNotDependOnOutgoingAdapters =
        noClasses().that().resideInAPackage(ADAPTER_IN)
            .should().dependOnClassesThat().resideInAPackage(ADAPTER_OUT)
            .because("adapter/in must not depend on adapter/out");

    /**
     * core/domain must depend only on port/out — never on any adapter.
     * (hexagonal-architecture.md: "core/domain depends on port/out — never on adapter/out directly")
     */
    @ArchTest
    static final ArchRule domainMustNotDependOnAdapters =
        noClasses().that().resideInAPackage(CORE_DOMAIN)
            .should().dependOnClassesThat().resideInAnyPackage(ADAPTER_IN, ADAPTER_OUT)
            .because("core/domain must only depend on port/out interfaces — never on adapters");

    /**
     * adapter/out must not depend on core/domain or adapter/in.
     * (hexagonal-architecture.md: "adapter/out implements port/out — never imports from core/domain")
     */
    @ArchTest
    static final ArchRule outgoingAdaptersMustNotDependOnDomainOrIncomingAdapters =
        noClasses().that().resideInAPackage(ADAPTER_OUT)
            .should().dependOnClassesThat().resideInAnyPackage(CORE_DOMAIN, ADAPTER_IN)
            .because("adapter/out must not depend on core/domain or adapter/in");

    // ─────────────────────────────────────────────────────────────────────
    // Port purity rules  (ports-layer.md: "Ports must not import Spring, JPA, or any infrastructure type")
    // ─────────────────────────────────────────────────────────────────────

    /**
     * port/in interfaces must be pure Java — no Spring or JPA imports.
     */
    @ArchTest
    static final ArchRule inboundPortsMustBePureJava =
        noClasses().that().resideInAPackage(PORT_IN)
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework..",
                "jakarta.persistence.."
            )
            .because("port/in interfaces must be pure Java — no Spring or JPA dependencies");

    /**
     * port/out interfaces must be pure Java — no Spring or JPA imports.
     */
    @ArchTest
    static final ArchRule outboundPortsMustBePureJava =
        noClasses().that().resideInAPackage(PORT_OUT)
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework..",
                "jakarta.persistence.."
            )
            .because("port/out interfaces must be pure Java — no Spring or JPA dependencies");

    // ─────────────────────────────────────────────────────────────────────
    // Naming rules  (java-spring-standards.md — Naming Conventions)
    // ─────────────────────────────────────────────────────────────────────

    /**
     * All classes in port/in must have names ending in "UseCase".
     */
    @ArchTest
    static final ArchRule inboundPortsNamingConvention =
        classes().that().resideInAPackage(PORT_IN)
            .should().haveSimpleNameEndingWith("UseCase")
            .because("port/in interfaces must be named with the UseCase suffix");

    /**
     * Classes in adapter/out database reader/ must end with "ReadRepository".
     */
    @ArchTest
    static final ArchRule readRepositoryNamingConvention =
        classes().that().resideInAPackage(ROOT + ".adapter.out..reader..")
            .and().areNotInterfaces()
            .should().haveSimpleNameEndingWith("ReadRepository")
            .because("read-side adapters must be named {Domain}ReadRepository");

    /**
     * Classes in adapter/out database writer/ must end with "WriteRepository".
     */
    @ArchTest
    static final ArchRule writeRepositoryNamingConvention =
        classes().that().resideInAPackage(ROOT + ".adapter.out..writer..")
            .and().areNotInterfaces()
            .should().haveSimpleNameEndingWith("WriteRepository")
            .because("write-side adapters must be named {Domain}WriteRepository");

    /**
     * JPA entities must reside in an entity sub-package of adapter/out.
     * (adapter-layer.md: "Place JPA entities in adapter/out/{type}/entity/")
     */
    @ArchTest
    static final ArchRule jpaEntitiesMustResideInEntityPackage =
        classes().that().areAnnotatedWith("jakarta.persistence.Entity")
            .should().resideInAPackage(ROOT + ".adapter.out..entity..")
            .because("JPA entities must be confined to adapter/out/{type}/entity/");

    // ─────────────────────────────────────────────────────────────────────
    // JPA entity boundary rule  (java-spring-standards.md: "Never expose JPA entities outside adapter/out")
    // ─────────────────────────────────────────────────────────────────────

    /**
     * JPA entities (@Entity) must not be referenced outside adapter/out.
     */
    @ArchTest
    static final ArchRule jpaEntitiesMustNotLeakBeyondAdapterOut =
        noClasses().that().resideOutsideOfPackage(ADAPTER_OUT)
            .should().dependOnClassesThat().areAnnotatedWith("jakarta.persistence.Entity")
            .because("JPA entities must never be referenced outside adapter/out");

    // ─────────────────────────────────────────────────────────────────────
    // Full layered architecture — combined assertion
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Validates the complete hexagonal layer stack as a single rule.
     * Mirrors all dependency rules in hexagonal-architecture.md.
     */
    @ArchTest
    static final ArchRule hexagonalLayerDependencies =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("IncomingAdapters").definedBy(ADAPTER_IN)
            .layer("OutgoingAdapters").definedBy(ADAPTER_OUT)
            .layer("Domain").definedBy(CORE_DOMAIN)
            .layer("InboundPorts").definedBy(PORT_IN)
            .layer("OutboundPorts").definedBy(PORT_OUT)
            .layer("Configuration").definedBy(CONFIGURATION)

            .whereLayer("IncomingAdapters").mayOnlyAccessLayers("InboundPorts")
            .whereLayer("Domain").mayOnlyAccessLayers("OutboundPorts")
            .whereLayer("OutgoingAdapters").mayOnlyAccessLayers("OutboundPorts")
            .whereLayer("InboundPorts").mayNotAccessAnyLayer()
            .whereLayer("OutboundPorts").mayNotAccessAnyLayer();
}
