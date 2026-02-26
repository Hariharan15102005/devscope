export default function Loader({ label = 'Loading...' }) {
  return (
    <div className="ds-loader-wrap" role="status" aria-live="polite">
      <div className="ds-loader" />
      <span>{label}</span>
    </div>
  );
}
