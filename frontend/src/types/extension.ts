export interface FixedExtension {
  id: number
  name: string
  checked: boolean
}

export interface CustomExtension {
  id: number
  name: string
}

export interface ExtensionsResponse {
  fixedExtensions: FixedExtension[]
  customExtensions: CustomExtension[]
}

export interface FixedExtensionUpdateRequest {
  checked: boolean
}

export interface CustomExtensionCreateRequest {
  name: string
}
