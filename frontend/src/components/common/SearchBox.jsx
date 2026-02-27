export default function SearchBox({ value, onChange, placeholder = 'Search...' }) {
  return (
    <label className="ds-filter-field ds-search-box">
      <span>Search</span>
      <input
        type="text"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
      />
    </label>
  );
}
