import { queryOptions } from '@tanstack/react-query'
import { TenantResponse } from './tenant.type'
import { gql, GraphQLClient } from 'graphql-request'

export class TenantApi {
    constructor(private readonly client: GraphQLClient) {}

    getAllTenants() {
        const query = gql`
            query GetAllTenants {
                tenants {
                    id
                    name
                    code
                    contact
                    country
                    status
                    created
                }
            }
        `

        return queryOptions({
            queryKey: ['tenants'],
            queryFn: () => this.client.request<{ tenants: TenantResponse[] }>(query),
        })
    }
}
