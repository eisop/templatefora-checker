package org.checkerframework.checker.templatefora;

import org.checkerframework.common.basetype.BaseTypeChecker;

/**
 * This is the entry point for pluggable type-checking.
 *
 * <p>{@code jdk.astub}, in this same directory, is loaded automatically: no {@code @StubFiles}
 * annotation is needed for that specific filename.
 */
public class TemplateforaChecker extends BaseTypeChecker {}
