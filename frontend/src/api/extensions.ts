import { apiClient } from './client'
import type {
  CustomExtension,
  CustomExtensionCreateRequest,
  ExtensionsResponse,
  FixedExtension,
  FixedExtensionUpdateRequest,
} from '../types/extension'

const BASE = '/api/extensions'

export const extensionsApi = {
  getAll: () => apiClient.get<ExtensionsResponse>(BASE),

  updateFixed: (id: number, payload: FixedExtensionUpdateRequest) =>
    apiClient.patch<FixedExtension>(`${BASE}/fixed/${id}`, payload),

  createCustom: (payload: CustomExtensionCreateRequest) =>
    apiClient.post<CustomExtension>(`${BASE}/custom`, payload),

  deleteCustom: (id: number) => apiClient.delete<void>(`${BASE}/custom/${id}`),
}
