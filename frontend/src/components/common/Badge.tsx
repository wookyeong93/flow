interface BadgeProps {
  label: string
  onRemove?: () => void
}

export function Badge({ label, onRemove }: BadgeProps) {
  return (
    <span className="inline-flex items-center gap-1 rounded-full bg-gray-100 px-3 py-1 text-sm">
      {label}
      {onRemove && (
        <button
          type="button"
          onClick={onRemove}
          aria-label={`${label} 삭제`}
          className="text-gray-500 hover:text-gray-800 cursor-pointer"
        >
          ×
        </button>
      )}
    </span>
  )
}
