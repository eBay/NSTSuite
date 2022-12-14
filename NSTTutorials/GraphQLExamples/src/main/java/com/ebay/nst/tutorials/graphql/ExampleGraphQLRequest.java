package com.ebay.nst.tutorials.graphql;

import com.ebay.nst.graphql.NSTGraphQLRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExampleGraphQLRequest extends NSTGraphQLRequest {

    // Sourced from: https://docs.gdc.cancer.gov/API/Users_Guide/GraphQL_Examples/
    public ExampleGraphQLRequest() {
        super("query PROJECTS_EDGES($filters_2: FiltersArgument) { explore { ssms { hits(filters: $filters_2) { total edges { node { ssm_id gene_aa_change } } } } } }");

        // Adding variables:
        // {"filters_2": {"op":"in","content":{"field":"consequence.transcript.gene.gene_id","value":["ENSG00000155657"]}}}

        Map<String, Object> content = new HashMap<>();
        content.put("field", "consequence.transcript.gene.gene_id");
        content.put("value", Arrays.asList("ENSG00000155657"));

        Map<String, Object> filters = new HashMap<>();
        filters.put("op", "in");
        filters.put("content", content);

        Map<String, Object> variables = new HashMap<>();
        variables.put("filters_2", filters);

        this.setVariables(variables);
    }

}
