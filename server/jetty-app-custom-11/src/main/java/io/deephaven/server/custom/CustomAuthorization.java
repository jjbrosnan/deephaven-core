//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.server.custom;

import io.deephaven.auth.codegen.impl.InputTableServiceContextualAuthWiring;
import io.deephaven.server.auth.AllowAllAuthorizationProvider;

import javax.inject.Inject;

/**
 * Simple authorization that "allows all" except "denys all" for the {@link #getInputTableServiceContextualAuthWiring()
 * input table service}.
 */
public final class CustomAuthorization extends AllowAllAuthorizationProvider {

    @Inject
    public CustomAuthorization() {}

    @Override
    public InputTableServiceContextualAuthWiring getInputTableServiceContextualAuthWiring() {
        // Disable input table service
        return new InputTableServiceContextualAuthWiring.DenyAll();
    }
}
