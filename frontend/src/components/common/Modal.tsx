interface ModalProps {
  message: string
  onClose: () => void
}

export function Modal({ message, onClose }: ModalProps) {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-80 space-y-4">
        <p className="text-sm text-gray-800">{message}</p>
        <div className="flex justify-end">
          <button
            type="button"
            onClick={onClose}
            className="rounded bg-blue-600 px-4 py-2 text-white text-sm"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  )
}
